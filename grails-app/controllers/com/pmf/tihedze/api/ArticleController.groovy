package com.pmf.tihedze.api

import com.pmf.tihedze.repozitorijradova.Article
import com.pmf.tihedze.repozitorijradova.commands.article.CreateArticleCommand
import com.pmf.tihedze.repozitorijradova.commands.article.UpdateArticleCommand
import com.pmf.tihedze.repozitorijradova.exceptions.ArticleNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.AuthorNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.VolumeNotFoundException
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
        final def article = articleService.getById(uuid)
        successResponse(article)
    }

    def create(CreateArticleCommand command) {
        final def article = articleService.create(command)
        successResponse(article)
    }

    def update(String id, UpdateArticleCommand command) {
        final def uuid = UUID.fromString(id)
        final def article = articleService.update(command, uuid)
        successResponse(article)
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
