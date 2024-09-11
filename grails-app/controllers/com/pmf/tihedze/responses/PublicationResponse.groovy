package com.pmf.tihedze.responses

import com.pmf.tihedze.repozitorijradova.Publication

class PublicationResponse {
    String id
    String title
    String source
    List<VolumeResponse> volumes

    PublicationResponse(Publication publication) {
        id = publication.id.toString()
        title = publication.name
        source = publication.source
        volumes = publication.volumes.collect { new VolumeResponse(it)}
    }
}
