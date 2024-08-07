package com.pmf.tihedze.repozitorijradova.commands.publication

import grails.validation.Validateable

class UpdatePublicationCommand implements Validateable {
    String name
    String volume
    String issue

    static constraints = {
        name(blank: false, nullable: false)
        volume(blank: false, nullable: false)
        issue(blank: false, nullable: false)
    }
}
