package com.pmf.tihedze.repozitorijradova.commands.volume

import grails.validation.Validateable

class RemoveArticlesCommand implements Validateable{
    List<String> articleIds

    static constraints = {
        articleIds validator: {
            !it.isEmpty()
        }
    }
}
