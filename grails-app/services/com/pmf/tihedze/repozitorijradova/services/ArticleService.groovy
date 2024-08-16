package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Article
import com.pmf.tihedze.repozitorijradova.Author
import com.pmf.tihedze.repozitorijradova.Publication
import com.pmf.tihedze.repozitorijradova.Volume
import com.pmf.tihedze.repozitorijradova.commands.article.CreateArticleCommand
import com.pmf.tihedze.repozitorijradova.commands.article.UpdateArticleCommand
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
            final String rawQuery = """
            select id, title, summary, publication_id
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

        article.authors.addAll(authors)

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

        final def authors = Author.getAll(command.authorIds)

        if (authors.isEmpty()) {
            throw new AuthorNotFoundException('No authors found with passed ids')
        }

        article.authors.removeAll(authors)

        final def volume = Volume.get(command.volumeId)

        if (volume == null) {
            throw new VolumeNotFoundException('No publication found with passed id')
        }

        article.volume.removeFromArticles(article)
        article.authors.addAll(authors)
        volume.addToArticles(article)

        article.summary = command.summary
        article.title = command.title

        article.save(flush: true)
    }

    void delete(UUID id) {
        Article.get(id).delete()
    }

    Article getById(UUID id) {
        Article.get(id)
    }
}
