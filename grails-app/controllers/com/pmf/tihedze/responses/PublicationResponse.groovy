package com.pmf.tihedze.responses

import com.pmf.tihedze.repozitorijradova.Publication

class PublicationResponse {
    String id
    String name
    List<VolumeResponse> volumes
    PublicationResponse(Publication publication) {
        id = publication.id.toString()
        name = publication.name
        volumes = publication.volumes.collect { new VolumeResponse(it)}
    }
}
