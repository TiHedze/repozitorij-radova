package com.pmf.tihedze.responses

import com.pmf.tihedze.repozitorijradova.Publication

class PublicationResponse {
    String id
    String name
    String volume
    String issue
    PublicationResponse(Publication publication) {
        id = publication.id.toString()
        name = publication.name
        volume = publication.volume
        issue = publication.issue
    }
}
