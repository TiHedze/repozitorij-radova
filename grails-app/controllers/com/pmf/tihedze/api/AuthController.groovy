package com.pmf.tihedze.api

import org.springframework.http.HttpStatus

class AuthController {
    static responseFormats = ['json']
    static namespace = 'v1'

    private static Integer ONE_DAY = 24 * 3600 * 1000

    def userService

    def login(String username, String password) {
        def token = userService.login(username, password)
        def now = new Date()
        respond([status: HttpStatus.OK], [token: token, exipiresAt: now.getTime() + ONE_DAY])
    }

    def register(String username, String password, String publicationName) {
        userService.register(username, password, publicationName)
    }
}
