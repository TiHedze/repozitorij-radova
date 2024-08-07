package com.pmf.tihedze.responses

import com.pmf.tihedze.repozitorijradova.Article

class ArticleResponse {
    String id
    String title
    String summary
    PublicationResponse publication
    List<AuthorResponse> authors

    ArticleResponse(Article article) {
        id = article.id.toString()
        title = article.title
        summary = article.summary
        publication = new PublicationResponse(article.publication)
        authors = article.authors.collect { new AuthorResponse(it)}
    }
}
