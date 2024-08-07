package com.pmf.tihedze.repozitorijradova.commands.article

import grails.validation.Validateable

class UpdateArticleCommand implements Validateable{
    String summary
    String title
    ArrayList<UUID> authorIds
    UUID publicationId

    static constraints = {
        authorIds validator: {
            !it.authorIds.isEmpty()
        }
        summary blank: false, nullable: false
        title blank: false, nullable: false
        publicationId nullable: false
    }
}
