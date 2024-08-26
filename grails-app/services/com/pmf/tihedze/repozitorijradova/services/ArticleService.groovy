package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Article
import com.pmf.tihedze.repozitorijradova.Author
import com.pmf.tihedze.repozitorijradova.Publication
import com.pmf.tihedze.repozitorijradova.Volume
import com.pmf.tihedze.repozitorijradova.commands.article.AddAuthorsCommand
import com.pmf.tihedze.repozitorijradova.commands.article.CreateArticleCommand
import com.pmf.tihedze.repozitorijradova.commands.article.RemoveAuthorsCommand
import com.pmf.tihedze.repozitorijradova.commands.article.UpdateArticleCommand
import com.pmf.tihedze.repozitorijradova.exceptions.ArticleNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.AuthorNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.PublicationNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.VolumeNotFoundException
import grails.gorm.transactions.Transactional
import org.hibernate.SessionFactory
import org.hibernate.query.NativeQuery
import org.hibernate.type.PostgresUUIDType

@Transactional
class ArticleService {
    SessionFactory sessionFactory

    List<Article> getArticles(String query) {
        if (query != null) {
            query = query.replace(' ', " & ")
            final String rawQuery = """
            select id, title, summary, volume_id
            from articles 
            where summary_search_vector @@ to_tsquery('english', :query)
            """

            final session = sessionFactory.currentSession
            final NativeQuery<Article> sqlQuery = session.createNativeQuery(rawQuery)
            final List<Article> results = sqlQuery.with {
                addEntity(Article)
                setParameter('query', query)
                list()
            }
            return results
        }
        Article.findAll()
    }

    Article create(CreateArticleCommand command) {

        def authors = Author.getAll(command.authorIds)

        if (authors.isEmpty()) {
            throw new AuthorNotFoundException('No authors found with passed ids')
        }

        def volume = Volume.get(command.volumeId)

        if (volume == null) {
            throw new VolumeNotFoundException('No volume found with passed id')
        }

        def article = new Article(title: command.title, summary: command.summary, volume: volume)

        authors.each {article.addToAuthors(it)}


        Article result = article.save(flush:true)

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

    Article addAuthors(AddAuthorsCommand command,UUID id) {
        addOrRemoveAuthors(command.authorIds, id)
    }

    Article removeAuthors(RemoveAuthorsCommand command, UUID id) {
        addOrRemoveAuthors(command.authorIds,id, true)
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

    private static Article addOrRemoveAuthors(List<String> authorIds, UUID articleId, boolean delete = false) {
        final def authors = Author.getAll(authorIds.collect { UUID.fromString(it)})

        if (authors == null) {
            throw new AuthorNotFoundException('No authors found for provided ids')
        }

        final def article = Article.get(articleId)

        if (delete) {
            authors.each {article.removeFromAuthors(it)}
        } else {
            authors.each {article.addToAuthors(it)}
        }
        article.save(flush: true)
    }
}
