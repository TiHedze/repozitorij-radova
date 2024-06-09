package com.pmf.tihedze.repozitorijradova.commands.author

import grails.validation.Validateable

class CreateAuthorCommand implements Validateable {
    String firstName
    String lastName

    static constraints = {
        firstName(blank: false, nullable: false)
        lastName(blank: false, nullable:false)
    }
}
