package com.pmf.tihedze.api.v1

import com.pmf.tihedze.api.BaseController
import com.pmf.tihedze.repozitorijradova.Publication
import com.pmf.tihedze.repozitorijradova.commands.publication.CreatePublicationCommand
import com.pmf.tihedze.repozitorijradova.commands.publication.UpdatePublicationCommand
import com.pmf.tihedze.repozitorijradova.services.ArticleService
import com.pmf.tihedze.repozitorijradova.services.PublicationService
import com.pmf.tihedze.responses.PublicationResponse
import org.apache.tools.ant.taskdefs.condition.Http
import org.springframework.http.HttpStatus

class PublicationController extends BaseController {
    static namespace = 'v1'

    PublicationService publicationService
    ArticleService articleService

    def create(CreatePublicationCommand command) {
        final def publication = publicationService.create(command)
        successResponse(publication)
    }

    def getById(String id) {
        final def uuid = UUID.fromString(id)
        final def publication = publicationService.getById(uuid)
        successResponse(publication)
    }

    def update(String id, UpdatePublicationCommand command) {
        final def uuid = UUID.fromString(id)
        final def publication = publicationService.update(command, uuid)
        successResponse(publication)
    }

    def getAll() {
        final def publications = publicationService.getAll()
        successResponse(publications)
    }

    def delete(String id) {
        final def uuid = UUID.fromString(id)
        publicationService.delete(uuid)
        respond([status: HttpStatus.ACCEPTED])
    }

    def populateDatabase() {
        articleService.populateDatabase()
        respond([status: HttpStatus.OK], [:])
    }

    private def successResponse(Publication publication) {
        final def response = new PublicationResponse(publication)
        respond([status: HttpStatus.OK], response)
    }

    private def successResponse(List<Publication> publications) {
        final def response = publications.collect { new PublicationResponse(it)}
        respond([status: HttpStatus.OK], response)
    }
}
