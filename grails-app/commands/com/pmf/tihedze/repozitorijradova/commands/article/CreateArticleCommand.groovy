package com.pmf.tihedze.repozitorijradova.commands.article

import grails.validation.Validateable

class CreateArticleCommand implements Validateable {
    String summary
    String title
    ArrayList<UUID> authorIds
    UUID volumeId

    static constraints = {
        authorIds validator: {
            !it.isEmpty()
        }
        summary blank: false, nullable: false
        title blank: false, nullable: false
        volumeId nullable: false
    }
}
