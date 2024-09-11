package com.pmf.tihedze.api.v1

import com.pmf.tihedze.api.BaseController
import com.pmf.tihedze.repozitorijradova.Volume
import com.pmf.tihedze.repozitorijradova.commands.volume.CreateVolumeCommand
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

    def getByQuery(String publicationName, String issue) {
        if ((publicationName == null || publicationName.isBlank()) && (issue != null && !issue.isBlank()) ) {
                return successResponse(volumeService.getAllByIssue(publicationName,issue))
        }
        if (issue != null && !issue.isBlank()) {
            return successResponse(volumeService.getByPublicationNameAndIssue(publicationName, issue))
        }

        if (publicationName != null && !publicationName.isBlank()) {
            return successResponse(volumeService.getAllByPublicationName(publicationName))
        }
        return successResponse(volumeService.getAll())
    }

    def create(CreateVolumeCommand command) {
        final def result = volumeService.create(command)
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
