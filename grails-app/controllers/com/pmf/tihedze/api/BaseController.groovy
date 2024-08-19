package com.pmf.tihedze.api

import com.pmf.tihedze.repozitorijradova.exceptions.ArticleNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.AuthorNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.VolumeNotFoundException
import org.springframework.http.HttpStatus

abstract class BaseController {
    static responseFormats = ['json']

    def handleRuntimeException(RuntimeException exception) {
        internalServerError(exception)
    }

    def handleArticleNotFoundException(ArticleNotFoundException exception) {
        notFound(exception)
    }

    def handleAuthorNotFoundException(AuthorNotFoundException exception) {
        notFound(exception)
    }

    def handleVolumeNotFoundException(VolumeNotFoundException exception) {
        notFound(exception)
    }

    private def notFound(RuntimeException exception) {
        respond([status: HttpStatus.NOT_FOUND], [message: exception.message])
    }

    private def internalServerError(RuntimeException exception) {
        respond([status: HttpStatus.INTERNAL_SERVER_ERROR], [message: exception.message])
    }
}
