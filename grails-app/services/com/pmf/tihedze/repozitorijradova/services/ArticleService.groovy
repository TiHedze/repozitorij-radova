package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Article
import com.pmf.tihedze.repozitorijradova.Author
import com.pmf.tihedze.repozitorijradova.Publication
import com.pmf.tihedze.repozitorijradova.Volume
import com.pmf.tihedze.repozitorijradova.commands.article.*
import com.pmf.tihedze.repozitorijradova.exceptions.ArticleNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.AuthorNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.VolumeNotFoundException
import grails.gorm.transactions.NotTransactional
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
        if (query != null && !query.isBlank() && !query.isEmpty()) {
            query = query.replace(' ', " & ")
            final String rawQuery = """
            select id, title, summary, volume_id
            from articles 
            where summary_search_vector @@ to_tsquery('english', :query)
            """

            final session = sessionFactory.currentSession
            final NativeQuery<Article> sqlQuery = session.createNativeQuery(rawQuery)
            final def results = sqlQuery.with {
                addEntity(Article)
                setParameter('query', query)
                list()
            }
            return results
        }
        return Article.findAll {it }
    }

    def getByQuery(ArticleQueryCommand command) {
        def articles;
        if (command.summaryText != null && !command.summaryText.isBlank()) {
            final String rawQuery = """
            select id, title, summary, volume_id
            from articles 
            where summary_search_vector @@ to_tsquery('english', :query)
            """

            final session = sessionFactory.currentSession
            final NativeQuery<Article> sqlQuery = session.createNativeQuery(rawQuery)
            articles = sqlQuery.with {
                addEntity(Article)
                setParameter('query', command.summaryText)
                list()
            } as List
        }

        if (!articles?.empty) {
            return articles.findAll { article ->
                def matchedAuthors = []
                def partialPublicationName = command.publicationName ?: ""
                def partialVolumeName = command.volumeName ?: ""
                def partialArticleName = command.articleName ?: ""

                def publicationMatches = !partialPublicationName || article.volume.publication.title.contains(partialPublicationName)
                def volumeMatches = !partialVolumeName || article.volume.volume.contains(partialVolumeName)
                def articleNameMatches = !partialArticleName || article.title.contains(partialArticleName)

                if (command.authorName) {
                matchedAuthors = article.authors.findAll { author ->
                    "${author.firstName.toLowerCase()} ${author.lastName.toLowerCase()}".contains(command.authorName)
                    }
                } else {
                    matchedAuthors = article.authors.findAll()
                }
                    publicationMatches && volumeMatches && articleNameMatches && !matchedAuthors.empty
            }
        }
        def results = Article.createCriteria().list {
            // Join with related entities
            createAlias('authors', 'a')
            createAlias('volume', 'v')
            createAlias('v.publication', 'p')

            // Apply filter based on publication name if provided
            if (command.publicationName) {
                ilike('p.name', "%${command.publicationName}%")
            }

            if (command.authorName) {
            ilike('a.firstName', "%${authorName}%")
            ilike('a.lastName', "%${authorName}%")
            }

            if (command.volumeName) {
                ilike('v.volume',"%${command.volumeName}%")
            }

            if (command.articleName) {
                ilike('a.title',"%${command.articleName}%")
            }
        }

        return results
    }
    @NotTransactional
    Article create(CreateArticleCommand command) {

        def authors = Author.getAll(command.authorIds)

        if (authors.isEmpty()) {
            throw new AuthorNotFoundException('No authors found with passed ids')
        }

        def article = new Article(title: command.title, summary: command.summary, url: command.url)

        authors.each {article.addToAuthors(it)}

        def result = Article.withSession { session ->
            session.beginTransaction()
            session.setFlushMode(FlushMode.COMMIT)
            article.save(flush:true, validate: false)
            session.joinTransaction()
            return article;

        }

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
        def xml = new URL('https://hrcak.srce.hr/oai/?verb=ListRecords&metadataPrefix=oai_dc&set=journal:27').text
        def slurper = new XmlSlurper()
                .parseText(xml)
                .declareNamespace(
                        oai_dc: 'http://www.openarchives.org/OAI/2.0/oai_dc/',
                        dc: 'http://purl.org/dc/elements/1.1/'
                )

        def publications = []
        def authors = []
        def issues = []
        def volumes = []
        def articles = []

        slurper.ListRecords.record.each {
            it.metadata.'oai_dc:dc'.'dc:creator'*.each {
                def line = it.text()
                def indexOfLastWhitespace = line.lastIndexOf(' ');
                def firstName = line.substring(0, indexOfLastWhitespace)
                def lastName = line.substring(indexOfLastWhitespace + 1)
                authors << new Author(firstName: firstName, lastName: lastName)
            }

            publications << new Publication(name: it.metadata.'oai_dc'.'dc:source'*. where { } )
        }

    }
}
