package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Author
import com.pmf.tihedze.repozitorijradova.commands.author.CreateAuthorCommand
import grails.gorm.transactions.Transactional

@Transactional
class AuthorService {
    def dataSource

    def create(CreateAuthorCommand command) {
        var author = new Author(firstName: command.firstName, lastName: command.lastName)
        author.save()
    }

    def getById(UUID id) {
        Author.get(id);
    }
}
