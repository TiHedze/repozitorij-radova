package com.pmf.tihedze.api

import com.pmf.tihedze.repozitorijradova.Publication
import com.pmf.tihedze.repozitorijradova.Volume
import com.pmf.tihedze.repozitorijradova.services.VolumeService
import com.pmf.tihedze.responses.PublicationResponse
import org.springframework.http.HttpStatus

class VolumeController {
    static responseFormats = ['json']
    static namespace = 'v1'

    VolumeService volumeService

    def getById(String id) {
        def uuid = UUID.fromString(id)

    }

    private def successResponse(Volume volume) {
        final def response = new PublicationResponse(volume)
        respond([status: HttpStatus.OK], response)
    }

    private def successResponse(List<Volume> volumes) {
        final def response = volumes.collect { new PublicationResponse(it)}
        respond([status: HttpStatus.OK], response)
    }
}
