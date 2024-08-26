package com.pmf.tihedze.repozitorijradova.commands.article

import grails.validation.Validateable

class AddAuthorsCommand implements Validateable {
    List<String> authorIds

    static constraints = {
        authorIds validator: {
            !it.isEmpty()
        }
    }
}
