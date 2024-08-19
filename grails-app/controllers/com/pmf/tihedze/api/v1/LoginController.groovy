package com.pmf.tihedze.api.v1

import com.pmf.tihedze.api.BaseController
import com.pmf.tihedze.repozitorijradova.commands.auth.AuthCommand
import com.pmf.tihedze.repozitorijradova.services.UserService
import groovy.time.TimeCategory
import org.springframework.http.HttpStatus

class LoginController extends BaseController {
    static responseFormats = ['json']
    static namespace = 'v1'
    UserService userService

    def login(AuthCommand login) {
        def token = userService.login(login.username, login.password)
        use(TimeCategory) {
            respond([status: HttpStatus.OK], [token: token, exipiresAt: (new Date() + 1.day).getTime()])
        }
    }

    def register(AuthCommand register) {
        userService.register(register.username, register.password)
        respond([status: HttpStatus.OK], null)
    }
}
