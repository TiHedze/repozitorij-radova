package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Publication
import com.pmf.tihedze.repozitorijradova.commands.publication.CreatePublicationCommand
import com.pmf.tihedze.repozitorijradova.commands.publication.UpdatePublicationCommand
import com.pmf.tihedze.repozitorijradova.exceptions.PublicationNotFoundException
import grails.gorm.transactions.Transactional

@Transactional
class PublicationService {

    Publication getById(UUID id) {
        final def publication = Publication.findById(id)
        if (publication == null) {
            throw new PublicationNotFoundException("No publication with id ${id} exists")
        }
        publication
    }

    List<Publication> getAll(String query) {
        if (query != null) {
            return Publication.findAllByNameLike(query)
        }

        Publication.getAll()
    }

    Publication create(CreatePublicationCommand command) {
        final def publication = new Publication(name: command.title, source: command.source)
        publication.save(flush:true)
    }

    void delete(UUID id) {
        final def publication = Publication.findById(id)
        if (publication == null) {
            throw new PublicationNotFoundException('No publication found for the provided id')
        }
        publication.delete()
    }

    Publication update(UpdatePublicationCommand command, UUID id) {
        final def publication = Publication.findById(id)
        publication.name = command.name
        publication.save(flush: true)
    }
}
