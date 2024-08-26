package com.pmf.tihedze.responses

import com.pmf.tihedze.repozitorijradova.Article

class ArticleResponse {
    String id
    String title
    String summary
    List<AuthorResponse> authors
    String volumeName
    String volumeId

    ArticleResponse(Article article) {
        id = article.id.toString()
        title = article.title
        summary = article.summary
        authors = article.authors.collect { new AuthorResponse(it)}
        volumeName = article.volume.volume
        volumeId = article.volume.id.toString()
    }
}
