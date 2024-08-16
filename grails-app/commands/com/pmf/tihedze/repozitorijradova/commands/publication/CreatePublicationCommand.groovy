package com.pmf.tihedze.repozitorijradova.commands.publication

import grails.validation.Validateable

class CreatePublicationCommand implements Validateable {
    String name

    static constraints = {
        name(blank: false, nullable: false)
    }
}
