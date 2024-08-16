package com.pmf.tihedze.repozitorijradova.commands.auth

import grails.validation.Validateable

class AuthCommand implements Validateable {
    String username
    String password

    static constraints = {
        username(nullable: false, isBlank: false)
        password(nullable: false, isBlank: false)
    }
}
