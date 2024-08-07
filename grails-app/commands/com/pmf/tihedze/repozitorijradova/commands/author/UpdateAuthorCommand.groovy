package com.pmf.tihedze.repozitorijradova.commands.author

import grails.validation.Validateable

class UpdateAuthorCommand implements Validateable{
    String firstName
    String lastName

    static constraints = {
        firstName(blank: false, nullable: true)
        lastName(blank: false, nullable: true)
    }
}
