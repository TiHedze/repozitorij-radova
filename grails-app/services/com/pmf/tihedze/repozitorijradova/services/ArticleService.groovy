package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Article
import com.pmf.tihedze.repozitorijradova.Author
import com.pmf.tihedze.repozitorijradova.Publication
import com.pmf.tihedze.repozitorijradova.Volume
import com.pmf.tihedze.repozitorijradova.commands.article.ArticleQueryCommand
import com.pmf.tihedze.repozitorijradova.commands.article.CreateArticleCommand
import com.pmf.tihedze.repozitorijradova.commands.article.UpdateArticleCommand
import com.pmf.tihedze.repozitorijradova.exceptions.ArticleNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.AuthorNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.VolumeNotFoundException
import grails.gorm.transactions.Transactional
import groovy.xml.XmlSlurper
import groovyjarjarantlr.collections.List
import org.hibernate.FlushMode
import org.hibernate.SessionFactory
import org.hibernate.query.NativeQuery
import org.hibernate.type.PostgresUUIDType

@Transactional
class ArticleService {
    SessionFactory sessionFactory

    def getArticles(String query) {
        def articles = Article.findAll()
        articles
    }

    def getByQuery(ArticleQueryCommand command) {
        def articles = []
        if (command.summaryText != null && !command.summaryText.isBlank()) {
            final String rawQuery = """
            select id, title, summary, volume_id, url, year 
            from articles 
            where summary_search_vector @@ to_tsquery('english', :query)
            """

            final session = sessionFactory.currentSession
            final NativeQuery<Article> sqlQuery = session.createNativeQuery(rawQuery)
            articles = sqlQuery.with {
                addEntity(Article)
                setParameter('query', command.summaryText.trim().replace(" ", " & "))
                list()
            } as List
        }

        if (!articles?.empty) {
            return articles.findAll { article ->
                def matchedAuthors = []
                def partialPublicationName = command.publicationName ?: ""
                def partialVolumeName = command.volumeName ?: ""
                def partialArticleName = command.articleName ?: ""
                def partialYear = command.year as Integer

                def publicationMatches = !partialPublicationName || article.volume.publication.title.contains(partialPublicationName)
                def volumeMatches = !partialVolumeName || article.volume.volume.contains(partialVolumeName)
                def articleNameMatches = !partialArticleName || article.title.contains(partialArticleName)
                def yearMatches = !partialYear || article.year == partialYear

                if (command.authorName) {
                    matchedAuthors = article.authors.findAll { author ->
                    "${author.firstName.toLowerCase()} ${author.lastName.toLowerCase()}".contains(command.authorName.toLowerCase())
                    }
                } else {
                    matchedAuthors = article.authors.findAll()
                }
                    publicationMatches && volumeMatches && articleNameMatches && yearMatches &&!matchedAuthors.empty
            }
        }
        def results = Article.createCriteria().listDistinct {
            createAlias('authors', 'a')
            createAlias('volume', 'v')
            createAlias('v.publication', 'p')

            if (command.publicationName) {
                ilike('p.name', "%${command.publicationName}%")
            }

            if (command.authorName) {
                or {
                    ilike('a.firstName', "%${command.authorName}%")
                    ilike('a.lastName', "%${command.authorName}%")
                }
            }

            if (command.volumeName) {
                ilike('v.volume',"%${command.volumeName}%")
            }

            if (command.articleName) {
                ilike('title',"%${command.articleName}%")
            }

            if (command.year) {
                eq('year', command.year)
            }
        }

        if (!results) {
            return Article.findAll()
        }

        return results
    }
    Article create(CreateArticleCommand command) {

        def authors = Author.getAll(command.authorIds)

        if (authors.isEmpty()) {
            throw new AuthorNotFoundException('No authors found with passed ids')
        }

        def article = new Article(title: command.title, summary: command.summary, url: command.url, year: command.year)

        authors.each {article.addToAuthors(it)}

        def result = Article.withSession { session ->
            session.setFlushMode(FlushMode.COMMIT)
            article.save(flush:true, validate: false)
            session.joinTransaction()
            return article;
        }

        authors.each {
            it.addToArticles(article: article)
            }*.save(flus: true)

        final def query = "UPDATE articles SET summary_search_vector = (select to_tsvector('english', :summary))" +
                " WHERE id = :id"

        final def session = sessionFactory.currentSession

        final def nativeQuery = session.createNativeQuery(query)
        nativeQuery.with {
            setParameter('summary', article.summary)
            setParameter('id', article.id, PostgresUUIDType.INSTANCE)
            executeUpdate()
        }
        session.joinTransaction()
        result
    }

    Article update(UpdateArticleCommand command, UUID id) {
        final def article = Article.get(id)
        def summaryChanged = false

        if (command.authorIds != null && !command.authorIds?.isEmpty()){
            final def authors = Author.getAll(command.authorIds.collect {UUID.fromString(it)})

            if (authors.isEmpty()) {
                throw new AuthorNotFoundException('No authors found with the provided ids')
            }

            article.authors.each { article.removeFromAuthors(it)}
            authors.each { article.addToAuthors(it)}
        }

        if (command.summary != null && command.summary?.isBlank()) {
            article.summary = command.summary
            summaryChanged = true
        }

        if (command.title != null && !command.title.isEmpty()) {
            article.title = title
        }

        if (command.volumeId != null) {
            final def volume = Volume.get(command.volumeId)

            if (volume == null) {
                throw new VolumeNotFoundException('No volume found with the provided id')
            }
            article.volume = volume
        }
        if (!article.hasChanged())  {
            return article
        }
        final def result = article.save(flush: true)

        if (summaryChanged) {
            final def query = "UPDATE articles SET summary_search_vector = (select to_tsvector('english', :summary))" +
                    " WHERE id = :id"

            final def session = sessionFactory.currentSession

            final def nativeQuery = session.createNativeQuery(query)

            nativeQuery.with {
                setParameter('summary', command.summary)
                setParameter('id', article.id, PostgresUUIDType.INSTANCE)
                executeUpdate()
            }
            session.joinTransaction()
        }

        result
    }

    void delete(UUID id) {
        final def article = Article.get(id)
        if (article == null) {
            throw new ArticleNotFoundException("No article found for the provided id")
        }
        article.delete()
    }

    Article getById(UUID id) {
        Article.get(id)
    }

    def populateDatabase() {
        def session = sessionFactory.currentSession
        def xml = new URL('https://hrcak.srce.hr/oai/?verb=ListRecords&metadataPrefix=oai_dc&set=journal:27').text
        def (Publication publication,
        ArrayList<Volume> volumes,
        LinkedHashMap<String, ArrayList<String>> articleAuthor,
        LinkedHashMap<String, Author> authorMap) = parseXml(xml)
        def groupedVolumes = volumes
                .groupBy {"${it.volume};${it.issue}" }
                .collectEntries {key, value -> {
                    [key, value.collectMany {it.articles}]
                }}
        publication.save(flush:true)
        groupedVolumes.collect {
            def splitName = (it.key as GString).split(';')
            def volume = new Volume(volume: splitName[0], issue: splitName[1])
            Tuple.tuple(volume, it.value)
        }.each { volumeArticle ->
            volumeArticle[1].each { article ->
                    def authors = articleAuthor[article.url].collect { authorMap[it] }
                    authors.each {
                        article.addToAuthors(it)
                    }
                    volumeArticle[0].addToArticles(article)
                    volumeArticle[0].publication = publication  // Ensure the publication is set
                    volumeArticle[0].save(flush:true, failOnError: false)
                    // Add the volume to the publication
                    publication.addToVolumes(volumeArticle[0])
            }
        }
        publication.save(flush:true)

        final def query = "UPDATE articles SET summary_search_vector = (select to_tsvector('english', summary))"


        final def nativeQuery = session.createNativeQuery(query)

        nativeQuery.with {
            executeUpdate()
        }
        session.joinTransaction()
    }

    private static def parseXml(String xmlText) {
        def xml = new XmlSlurper().parseText(xmlText).declareNamespace(
                oai_dc: 'http://www.openarchives.org/OAI/2.0/oai_dc/',
                dc: 'http://purl.org/dc/elements/1.1/'
        )

        def more = xml.ListRecords.resumptionToken.text().isBlank()

        Publication publication = new Publication(name: xml.ListRecords.record.metadata.'oai_dc:dc'.'dc:source'[0].text(), source: 'Hrčak',volumes: [])
        def volumes = []
        def authorMap = [:]
        def articleAuthor = [:]
        def articles = []
        while(!more) {
            def token = xml.ListRecords.resumptionToken.text()
            xml.ListRecords.record.each { record ->
                def metadata = record.metadata.'oai_dc:dc'

                // Retrieve the URL where xml:lang="eng"
                def url = metadata.'dc:identifier'.find { it.@'xml:lang' == 'eng' }?.text() ?:
                        metadata.'dc:identifier'.first().text() ?: ''

                // Create Article object
                Article article = new Article(
                        title: metadata.'dc:title'.text(),
                        summary: metadata.'dc:description'?.text() ?: metadata.'dc:title'.text(),
                        url: url,
                        authors: [],
                        year: metadata.'dc:date'.text() as int
                )

                articleAuthor[article.url] = []

                articles << article

                // Create Author(s)
                metadata.'dc:creator'.each { creator ->
                    def authorName = creator.text().split(' ')
                    def authorFirstName = authorName[0] ?: 'Unknown'
                    def authorLastName = authorName.size() > 1 ? authorName[1..-1].join(' '): 'Unknown'
                    def author = new Author(firstName: authorFirstName, lastName: authorLastName)
                    if (!authorMap.containsKey("$author.firstName $author.lastName")) {
                        authorMap["$author.firstName $author.lastName"] = author
                    }
                    if (!articleAuthor.containsKey(article.url)) {
                        articleAuthor[article.url] = []
                    }
                    articleAuthor[article.url].add("$author.firstName $author.lastName")

                }

                // Create or retrieve Volume and associate it with the article
                    def volume = new Volume(
                            volume: metadata.'dc:source'.find { it.text().contains('Volume') }?.text(),
                            issue: metadata.'dc:source'.find { it.text().contains('Issue') }?.text(),
                            articles: [])

                volume.publication = publication
                volumes << volume
                volume.articles << article
                }
            more = xml.ListRecords.resumptionToken.text().isBlank()
            def raw = new URL("https://hrcak.srce.hr/oai/?verb=ListRecords&resumptionToken=$token").text
            xml = new XmlSlurper().parseText(raw).declareNamespace(
                    oai_dc: 'http://www.openarchives.org/OAI/2.0/oai_dc/',
                    dc: 'http://purl.org/dc/elements/1.1/'
            )
        }
        return Tuple.tuple(publication, volumes, articleAuthor, authorMap)
    }
}
