package com.pmf.tihedze.api.v1

import com.pmf.tihedze.api.BaseController
import com.pmf.tihedze.repozitorijradova.Volume
import com.pmf.tihedze.repozitorijradova.commands.volume.AddArticlesCommand
import com.pmf.tihedze.repozitorijradova.commands.volume.CreateVolumeCommand
import com.pmf.tihedze.repozitorijradova.commands.volume.RemoveArticlesCommand
import com.pmf.tihedze.repozitorijradova.services.VolumeService
import com.pmf.tihedze.responses.VolumeResponse
import org.springframework.http.HttpStatus

class VolumeController extends BaseController {
    static namespace = 'v1'

    VolumeService volumeService

    def getById(String id) {
        final def uuid = UUID.fromString(id)
        final def result = volumeService.getById(uuid)
        successResponse(result)
    }

    def getByQuery(String publicationName, String issue, String name) {
        Volume response
        if (name == null || name.isBlank()) {
            response = volumeService.getByPublicationNameAndIssue(publicationName, issue)
        }
        else if ((name == null || name.isBlank()) &&
            (issue == null || issue.isBlank()) &&
            (publicationName == null || publicationName.isBlank())) {
            throw new RuntimeException('No parameters passed for query')
        } else {
            response = volumeService.getByPublicationNameAndIssue(publicationName, issue)
        }

        successResponse(response)
    }

    def create(CreateVolumeCommand command) {
        final def result = volumeService.create(command)
        successResponse(result)
    }

    def addArticles(AddArticlesCommand command, String id) {
        final def uuid = UUID.fromString(id)
        final def result = volumeService.addArticles(command, uuid)
        successResponse(result)
    }

    def removeArticles(RemoveArticlesCommand command, String id) {
        final def uuid = UUID.fromString(id)
        final def result = volumeService.removeArticles(command, uuid)
        successResponse(result)
    }

    def delete(String id) {
        final def uuid = UUID.fromString(id)
        volumeService.delete(uuid)
        respond([status: HttpStatus.ACCEPTED], null)
    }

    def getAllByPublicationId(String id) {
        final def uuid = UUID.fromString(id)
        final def result = volumeService.getAllByPublicationId(uuid)
        successResponse(result)
    }

    def getAllByNameQuery(String query) {
        final def result = volumeService.getAllByPublicationName(query)
        successResponse(result)
    }

    private def successResponse(Volume volume) {
        final def response = new VolumeResponse(volume)
        respond([status: HttpStatus.OK], response)
    }


    private def successResponse(List<Volume> volumes) {
        final def response = volumes.collect { new VolumeResponse(it)}
        respond([status: HttpStatus.OK], response)
    }
}
