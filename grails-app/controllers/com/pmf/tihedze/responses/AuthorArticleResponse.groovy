package com.pmf.tihedze.responses

import com.pmf.tihedze.repozitorijradova.Author

class AuthorArticleResponse {
    String id
    String firstName
    String lastName

    AuthorArticleResponse(Author author) {
        id = author.id.toString()
        firstName = author.firstName
        lastName = author.lastName
    }
}
