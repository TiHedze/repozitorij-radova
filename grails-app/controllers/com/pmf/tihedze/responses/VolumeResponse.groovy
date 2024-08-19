package com.pmf.tihedze.responses

import com.pmf.tihedze.repozitorijradova.Volume

class VolumeResponse {
    UUID id
    String volume
    String issue
    List<ArticleResponse> articles
    PublicationResponse publication

    VolumeResponse(Volume volume) {

    }
}
