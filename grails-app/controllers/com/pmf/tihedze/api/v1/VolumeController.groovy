package com.pmf.tihedze.api.v1

import com.pmf.tihedze.api.BaseController
import com.pmf.tihedze.repozitorijradova.Volume
import com.pmf.tihedze.repozitorijradova.commands.volume.CreateVolumeCommand
import com.pmf.tihedze.repozitorijradova.services.VolumeService
import com.pmf.tihedze.responses.VolumeResponse
import org.springframework.http.HttpStatus

class VolumeController extends BaseController {
    static responseFormats = ['json']
    static namespace = 'v1'

    VolumeService volumeService

    def getById(String id) {
        final def uuid = UUID.fromString(id)
        final def response = volumeService.getById(uuid)
        successResponse(response)
    }

    def getByPublicationNameAndIssue(String publicationName, String issue) {
        final def response = volumeService.getByPublicationNameAndIssue(publicationName, issue)
        successResponse(response)
    }

    def create(CreateVolumeCommand command) {
        def result = volumeService.create()
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
