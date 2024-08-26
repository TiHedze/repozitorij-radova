package com.pmf.tihedze.repozitorijradova.commands.article

import grails.validation.Validateable

class RemoveAuthorsCommand implements Validateable {
    List<String> authorIds

    static constraints = {
        authorIds validator: {
            !it.isEmpty()
        }
    }
}
