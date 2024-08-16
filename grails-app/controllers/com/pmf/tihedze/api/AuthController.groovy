package com.pmf.tihedze.api

import com.pmf.tihedze.repozitorijradova.commands.auth.AuthCommand
import com.pmf.tihedze.repozitorijradova.services.UserService
import org.springframework.http.HttpStatus

class AuthController {
    static responseFormats = ['json']
    static namespace = 'v1'

    private static Integer ONE_DAY = 24 * 3600 * 1000

    UserService userService

    def login(AuthCommand login) {
        def token = userService.login(login.username, login.password)
        def now = new Date()
        respond([status: HttpStatus.OK], [token: token, exipiresAt: now.getTime() + ONE_DAY])
    }

    def register(AuthCommand register) {
        userService.register(register.username, register.password)
        respond([status: HttpStatus.OK], null)
    }
}
