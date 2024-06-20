package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Article
import com.pmf.tihedze.repozitorijradova.Author
import com.pmf.tihedze.repozitorijradova.Publication
import com.pmf.tihedze.repozitorijradova.commands.article.CreateArticleCommand
import com.pmf.tihedze.repozitorijradova.exceptions.AuthorNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.PublicationNotFoundException

class ArticleService {
    def sessionFactory

    def getArticles(String query) {
        if (query != null) {
            final String rawQuery = """
            select id, title, summary 
            from articles 
            where summary_search_vector @@ to_tsquery('english', :query)
            """
            final session = sessionFactory.currentSession
            final sqlQuery = session.createSQLQuery(rawQuery)

            final results = sqlQuery.with {
                addEntity(Article)
                setString('query', query)
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

        def article = new Article(title: command.title, summary: command.summary)
        article.
    }
}
