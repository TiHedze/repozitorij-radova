package com.pmf.tihedze.repozitorijradova.commands.article

import grails.validation.Validateable

class CreateArticleCommand implements Validateable {
    String summary
    String title
    ArrayList<UUID> authorIds
    UUID publicationId

    static constraints = {
        authorIds validator: {
            !it.authorIds.isEmpty()
        }
        summary blank: false
        title blank: false
        publicationId nullable: false
    }
}
