package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Author
import com.pmf.tihedze.repozitorijradova.commands.author.CreateAuthorCommand
import com.pmf.tihedze.repozitorijradova.commands.author.UpdateAuthorCommand
import com.pmf.tihedze.repozitorijradova.exceptions.AuthorNotFoundException
import com.pmf.tihedze.responses.AuthorResponse
import grails.gorm.transactions.Transactional

@Transactional
class AuthorService {

    def create(CreateAuthorCommand command) {
        final def author = new Author(firstName: command.firstName, lastName: command.lastName)
        author.save()
    }

    def getById(UUID id) {
        def author = Author.where{
            id == id
        }.join('articles').get()
        return new AuthorResponse(author)

    }

    def update(UUID id, UpdateAuthorCommand command) {
        final def author = Author.get(id)

        author.firstName = command.firstName
        author.lastName = command.lastName

        author.save()
    }

    def getAuthorByQuery(String query) {
        Author.createCriteria().list {
            or {
                ilike('firstName', "%$query%")
                ilike('lastName', "%$query%")
            }
        }
    }

    def delete(UUID id) {
        final def author = Author.get(id)
        if (author == null) {
            throw new AuthorNotFoundException('No author found for the provided id')
        }
        author.delete()
    }

    def getAll() {
        Author.getAll()
    }
}
