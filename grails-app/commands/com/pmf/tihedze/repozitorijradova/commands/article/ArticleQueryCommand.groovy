package com.pmf.tihedze.repozitorijradova.commands.article

import grails.validation.Validateable

class ArticleQueryCommand implements Validateable{
    String publicationName
    String authorName
    String volumeName
    String summaryText
    String articleName
    String year

    static constraints = {
        publicationName nullale: true, blank: true
        authorName nullale: true, blank: true
        volumeName nullale: true, blank: true
        summaryText nullale: true, blank: true
        articleName nullale: true, blank: true
        year nullable: true, blank: true
    }
}
