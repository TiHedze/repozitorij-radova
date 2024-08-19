package com.pmf.tihedze.responses

import com.pmf.tihedze.repozitorijradova.Volume

class VolumeResponse {
    String id
    String volumeName
    String issue
    List<ArticleResponse> articles
    String publicationName

    VolumeResponse(Volume volume) {
        id = volume.id.toString()
        volumeName = volume.volume
        issue = volume.issue
        articles = volume.articles.collect { new ArticleResponse(it)}
        publicationName = volume.publication.name
    }
}
