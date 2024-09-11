package com.pmf.tihedze.responses

import com.pmf.tihedze.repozitorijradova.Article

class ArticleAuthorResponse {
    String title
    String id

    ArticleAuthorResponse(Article article) {
        title = article.title
        id = article.id
    }
}
