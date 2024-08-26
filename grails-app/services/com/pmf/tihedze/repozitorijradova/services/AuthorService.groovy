package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Author
import com.pmf.tihedze.repozitorijradova.commands.author.CreateAuthorCommand
import com.pmf.tihedze.repozitorijradova.commands.author.UpdateAuthorCommand
import com.pmf.tihedze.repozitorijradova.exceptions.AuthorNotFoundException
import grails.gorm.transactions.Transactional

@Transactional
class AuthorService {

    def create(CreateAuthorCommand command) {
        final def author = new Author(firstName: command.firstName, lastName: command.lastName)
        author.save()
    }

    def getById(UUID id) {
        Author.get(id);
    }

    def update(UUID id, UpdateAuthorCommand command) {
        final def author = Author.get(id)

        author.firstName = command.firstName
        author.lastName = command.lastName

        author.save()
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
