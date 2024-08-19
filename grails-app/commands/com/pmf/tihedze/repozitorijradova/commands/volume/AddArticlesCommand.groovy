package com.pmf.tihedze.repozitorijradova.commands.volume

import grails.validation.Validateable

class AddArticlesCommand implements Validateable{
    List<String> articleIds
    String volumeId

    static constraints = {
        articleIds validator: {
            !it.isEmpty()
        }
        volumeId(nullable: false, blank: false)
    }
}
