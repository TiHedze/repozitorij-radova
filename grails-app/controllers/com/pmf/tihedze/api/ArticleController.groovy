package com.pmf.tihedze.api

import org.springframework.http.HttpStatus

class ArticleController {
    static responseFormats = ['json']
    static namespace = 'v1'

    def getAll() {
        respond ([status: HttpStatus.OK],[message: 'Hello world!'])
    }

    def handleNullPointerException(NullPointerException ex) {

    }
}
