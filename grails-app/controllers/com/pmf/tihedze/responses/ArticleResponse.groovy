package com.pmf.tihedze.responses

import com.pmf.tihedze.repozitorijradova.Article

class ArticleResponse {
    String id
    String title
    String summary
    List<AuthorResponse> authors
    VolumeResponse volume

    ArticleResponse(Article article) {
        id = article.id.toString()
        title = article.title
        summary = article.summary
        authors = article.authors.collect { new AuthorResponse(it)}
        volume = new VolumeResponse(article.volume)
    }
}
