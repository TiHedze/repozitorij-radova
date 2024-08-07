package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Article
import com.pmf.tihedze.repozitorijradova.Author
import com.pmf.tihedze.repozitorijradova.Publication
import com.pmf.tihedze.repozitorijradova.commands.article.CreateArticleCommand
import com.pmf.tihedze.repozitorijradova.commands.article.UpdateArticleCommand
import com.pmf.tihedze.repozitorijradova.exceptions.AuthorNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.PublicationNotFoundException
import grails.gorm.transactions.Transactional
import org.hibernate.SessionFactory
import org.hibernate.query.NativeQuery
import org.hibernate.type.PostgresUUIDType
import org.hibernate.type.Type

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

        return Article.findAll()
    }

    def create(CreateArticleCommand command) {

        def authors = Author.getAll(command.authorIds)

        if (authors.isEmpty()) {
            throw new AuthorNotFoundException('No authors found with passed ids')
        }

        def publication = Publication.get(command.publicationId)

        if (publication == null) {
            throw new PublicationNotFoundException('No publication found with passed id')
        }

        def article = new Article(title: command.title, summary: command.summary, publication: publication)

        for (def author : authors) {
            article.addToAuthors(author)
        }

        def result = article.save(flush: true)

        final def query = "UPDATE articles SET summary_search_vector = (select to_tsvector('english', :summary))" +
                " WHERE id = :id"

        def session = sessionFactory.currentSession

        def nativeQuery = session.createNativeQuery(query)

        nativeQuery.with {
            setParameter('summary', article.summary)
            setParameter('id', article.id, PostgresUUIDType.INSTANCE)
            executeUpdate()
        }

        session.joinTransaction()

        result
    }

    def update(UpdateArticleCommand command, UUID id) {
        def article = Article.get(id)

        def authors = Author.getAll(command.authorIds)

        if (authors.isEmpty()) {
            throw new AuthorNotFoundException('No authors found with passed ids')
        }

        article.authors.forEach { article.removeFromAuthors(it) }

        def publication = Publication.get(command.publicationId)

        if (publication == null) {
            throw new PublicationNotFoundException('No publication found with passed id')
        }

        article.publication.removeFromArticles(article)

        for (def author : authors) {
            article.addToAuthors(author)
        }

        publication.addToArticles(article)

        article.summary = command.summary
        article.title = command.title

        article.save()
    }

    def delete(UUID id) {
        Article.get(id).delete()
    }

    def getById(UUID id) {
        Article.get(id)
    }
}
