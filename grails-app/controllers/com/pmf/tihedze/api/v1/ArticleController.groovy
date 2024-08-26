package com.pmf.tihedze.api.v1

import com.pmf.tihedze.api.BaseController
import com.pmf.tihedze.repozitorijradova.Article
import com.pmf.tihedze.repozitorijradova.commands.article.AddAuthorsCommand
import com.pmf.tihedze.repozitorijradova.commands.article.CreateArticleCommand
import com.pmf.tihedze.repozitorijradova.commands.article.RemoveAuthorsCommand
import com.pmf.tihedze.repozitorijradova.commands.article.UpdateArticleCommand
import com.pmf.tihedze.repozitorijradova.services.ArticleService
import com.pmf.tihedze.responses.ArticleResponse
import org.springframework.http.HttpStatus

class ArticleController extends BaseController{
    static namespace = 'v1'

    ArticleService articleService

    def getAll(String query) {
        final def articles = articleService.getArticles(query)
        successResponse(articles)
    }

    def getById(String id) {
        final def uuid = UUID.fromString(id)
        final def result = articleService.getById(uuid)
        successResponse(result)
    }

    def create(CreateArticleCommand command) {
        final def result = articleService.create(command)
        successResponse(result)
    }

    def update(String id, UpdateArticleCommand command) {
        final def uuid = UUID.fromString(id)
        final def result = articleService.update(command, uuid)
        successResponse(result)
    }

    def addAuthors(AddAuthorsCommand command, String id) {
        final def uuid = UUID.fromString(id)
        final def result = articleService.addAuthors(command, uuid)
        successResponse(result)
    }

    def removeAuthors(RemoveAuthorsCommand command, String id) {
        final def uuid = UUID.fromString(id)
        final def result = articleService.removeAuthors(command, uuid)
        successResponse(result)
    }

    def delete(String id) {
        final def uuid = UUID.fromString(id)
        articleService.delete(uuid)
        respond([status: HttpStatus.ACCEPTED])
    }

    private def successResponse(Article article) {
        final def response = new ArticleResponse(article)
        respond([status: HttpStatus.OK], response)
    }

    private def successResponse(List<Article> articles) {
        final def response = articles.collect { new ArticleResponse(it)}
        respond([status: HttpStatus.OK], response)
    }
}
