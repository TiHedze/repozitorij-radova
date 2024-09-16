package com.pmf.tihedze.responses

import com.pmf.tihedze.repozitorijradova.Article

class ArticleResponse {
    String id
    String title
    String summary
    List<AuthorArticleResponse> authors
    String volumeName
    String volumeId
    String publicationName
    String publicationId
    String url

    ArticleResponse(Article article) {
        id = article.id.toString()
        title = article.title
        summary = article.summary
        authors = article.authors.collect { new AuthorArticleResponse(it)}
        volumeName = article.volume?.volume
        volumeId = article.volume?.id?.toString()
        publicationName = article.volume?.publication?.name
        publicationId = article.volume?.publication?.id?.toString()
        url = article.url
    }
}
