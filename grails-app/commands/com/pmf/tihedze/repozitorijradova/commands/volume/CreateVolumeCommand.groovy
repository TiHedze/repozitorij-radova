package com.pmf.tihedze.repozitorijradova.commands.volume

import grails.validation.Validateable

class CreateVolumeCommand implements Validateable {
    String volume
    String issue
    List<String> articleIds
    String publicationId

    static constraints = {
        volume(nullable: false, blank: false)
        issue(nullable: false, blank: false)
        publicationId(nullable: false, blank: false)
    }
}
