package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Publication
import com.pmf.tihedze.repozitorijradova.commands.publication.CreatePublicationCommand
import com.pmf.tihedze.repozitorijradova.commands.publication.UpdatePublicationCommand
import com.pmf.tihedze.repozitorijradova.exceptions.PublicationNotFoundException
import grails.gorm.transactions.Transactional

@Transactional
class PublicationService {

    def getById(UUID id) {
        final def publication = Publication.get(id)
        if (publication == null) {
            throw new PublicationNotFoundException("No publication with id ${id} exists")
        }
    }

    def getAll() {
        Publication.getAll()
    }

    def create(CreatePublicationCommand command) {
        def publication = new Publication(volume: command.volume, issue: command.issue, name: command.name)
        publication.save()
    }

    def delete(UUID id) {
        def publication = Publication.get(id)
        publication.delete()
    }

    def update(UpdatePublicationCommand command, UUID id) {
        def publication = Publication.get(id)

        publication.name = command.name
        publication.issue = command.issue
        publication.volume = command.volume

        publication.save()
    }
}
