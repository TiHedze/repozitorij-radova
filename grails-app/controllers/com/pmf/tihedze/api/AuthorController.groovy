package com.pmf.tihedze.api

import com.pmf.tihedze.repozitorijradova.commands.author.CreateAuthorCommand
import com.pmf.tihedze.repozitorijradova.services.AuthorService
import com.pmf.tihedze.responses.AuthorResponse
import org.springframework.http.HttpStatus

class AuthorController {
    static responseFormats = ['json']
    static namespace = 'v1'

    AuthorService authorService

    def create(CreateAuthorCommand command) {
        var author = authorService.create(command)
        var response = new AuthorResponse(author)
        respond([status: HttpStatus.OK], response)
    }

    def getById(String id) {
        var uuid = UUID.fromString(id)
        var author = authorService.getById(uuid)
        respond([status: HttpStatus.OK], author)
    }
}
