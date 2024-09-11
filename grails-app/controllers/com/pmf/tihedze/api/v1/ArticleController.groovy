package com.pmf.tihedze.api.v1

import com.pmf.tihedze.api.BaseController
import com.pmf.tihedze.repozitorijradova.Article
import com.pmf.tihedze.repozitorijradova.commands.article.ArticleQueryCommand
import com.pmf.tihedze.repozitorijradova.commands.article.CreateArticleCommand
import com.pmf.tihedze.repozitorijradova.commands.article.UpdateArticleCommand
import com.pmf.tihedze.repozitorijradova.services.ArticleService
import com.pmf.tihedze.responses.ArticleResponse
import org.springframework.http.HttpStatus

class ArticleController extends BaseController{
    static namespace = 'v1'

    ArticleService articleService

    def getAll() {
        final def articles = articleService.getArticles(query)
        successResponse(articles)
    }

    def getByQuery(ArticleQueryCommand query) {
        final def response = articleService.getByQuery(query)
        successResponse(response)
    }

    def getById(String id) {
        final def uuid = UUID.fromString(id)
        final def result = articleService.getById(uuid)
        successResponse(result)
    }

    def create(CreateArticleCommand command) {
        final def result = articleService.create(command)
        idResponse(result.id)
    }

    def update(String id, UpdateArticleCommand command) {
        final def uuid = UUID.fromString(id)
        final def result = articleService.update(command, uuid)
        idResponse(result.id)
    }

    def delete(String id) {
        final def uuid = UUID.fromString(id)
        articleService.delete(uuid)
        respond([status: HttpStatus.ACCEPTED])
    }

    def populateDatabase() {

    }

    private def successResponse(Article article) {
        final def response = new ArticleResponse(article)
        respond([status: HttpStatus.OK], response)
    }

    private def idResponse(UUID id) {
        respond([status:HttpStatus.OK], [id: id.toString()])
    }

    private def successResponse(List<Article> articles) {
        final def response = articles.collect { new ArticleResponse(it)}
        respond([status: HttpStatus.OK], response)
    }
}
