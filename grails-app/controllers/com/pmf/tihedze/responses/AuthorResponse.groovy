package com.pmf.tihedze.responses

import com.pmf.tihedze.repozitorijradova.Author

class AuthorResponse {
    String id
    String firstName
    String lastName
    List<ArticleAuthorResponse> articles

    AuthorResponse(Author author) {
        id = author.id.toString()
        firstName = author.firstName
        lastName = author.lastName
        articles = author.articles.collect { new ArticleAuthorResponse(it)}
    }
}
