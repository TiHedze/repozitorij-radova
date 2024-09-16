package com.pmf.tihedze.api

import com.pmf.tihedze.repozitorijradova.exceptions.ApplicationExceptionBase
import com.pmf.tihedze.repozitorijradova.exceptions.ArticleNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.AuthorNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.VolumeNotFoundException
import org.springframework.http.HttpStatus

abstract class BaseController {
    static responseFormats = ['json']

    def handleArticleNotFoundException(ArticleNotFoundException exception) {
        notFound(exception)
    }

    def handleAuthorNotFoundException(AuthorNotFoundException exception) {
        notFound(exception)
    }

    def handleVolumeNotFoundException(VolumeNotFoundException exception) {
        notFound(exception)
    }

    protected <T extends ApplicationExceptionBase> Object notFound(T exception) {
        respond([status: HttpStatus.NOT_FOUND], [message: exception.message])
    }

    def handleRuntimeException(Exception exception) {
        respond([status: HttpStatus.INTERNAL_SERVER_ERROR], [message: exception.message])
    }
}
