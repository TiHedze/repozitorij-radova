package com.pmf.tihedze.repozitorijradova.commands.publication

import grails.validation.Validateable

class CreatePublicationCommand implements Validateable {
    String title
    String source

    static constraints = {
        title(blank: false, nullable: false)
    }
}
