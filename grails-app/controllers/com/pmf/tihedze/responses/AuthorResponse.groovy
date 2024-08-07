package com.pmf.tihedze.responses

import com.pmf.tihedze.repozitorijradova.Author

class AuthorResponse {
    String id
    String firstName
    String lastName

    AuthorResponse(Author author) {
        id = author.id.toString()
        firstName = author.firstName
        lastName = author.lastName
    }
}
