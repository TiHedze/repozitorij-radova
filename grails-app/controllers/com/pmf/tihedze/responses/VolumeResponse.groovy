package com.pmf.tihedze.responses

import com.pmf.tihedze.repozitorijradova.Volume

class VolumeResponse {
    String id
    String volume
    String issue
    List<ArticleResponse> articles
    String publicationName

    VolumeResponse(Volume volume) {
        id = volume.id.toString()
        volume = volume.volume
        issue = volume.issue
        articles = volume.articles.collect { new ArticleResponse(it)}
        publicationName = volume.publication.name
    }
}
