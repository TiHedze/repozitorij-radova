package com.pmf.tihedze.api.v1

import com.pmf.tihedze.api.BaseController
import com.pmf.tihedze.repozitorijradova.Author
import com.pmf.tihedze.repozitorijradova.commands.author.CreateAuthorCommand
import com.pmf.tihedze.repozitorijradova.commands.author.UpdateAuthorCommand
import com.pmf.tihedze.repozitorijradova.services.AuthorService
import com.pmf.tihedze.responses.AuthorResponse
import org.springframework.http.HttpStatus

class AuthorController  extends BaseController {
    static namespace = 'v1'

    AuthorService authorService

    def create(CreateAuthorCommand command) {
        final def author = authorService.create(command)
        successResponse(author)
    }

    def getById(String id) {
        final def uuid = UUID.fromString(id)
        final def author = authorService.getById(uuid)
        successResponse(author)
    }

    def update(String id, UpdateAuthorCommand command) {
        final def uuid = UUID.fromString(id)
        final def author = authorService.update(uuid, command)
        successResponse(author)
    }

    def getAll() {
        final def authors = authorService.getAll()
        successResponse(authors)
    }

    def delete(String id) {
        final def uuid = UUID.fromString(id)
        authorService.delete(uuid)
        respond([status: HttpStatus.ACCEPTED])
    }

    private def successResponse(Author author) {
        final def response = new AuthorResponse(author)
        respond([status: HttpStatus.OK], response)
    }

    private def successResponse(List<Author> authors) {
        final def response = authors.collect { new AuthorResponse(it) }
        respond([status: HttpStatus.OK], response)
    }
}
