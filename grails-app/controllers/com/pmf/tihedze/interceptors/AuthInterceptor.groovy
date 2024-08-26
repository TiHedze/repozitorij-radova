package com.pmf.tihedze.interceptors

import com.pmf.tihedze.repozitorijradova.services.JwtService
import groovy.transform.CompileStatic
import org.springframework.http.HttpStatus

import java.text.SimpleDateFormat

@CompileStatic
class AuthInterceptor {
    int order = HIGHEST_PRECEDENCE + 1
    private final static String  AUTHORIZATION_HEADER ='Authorization'

    AuthInterceptor() {
        match(controller: 'article', action: 'create')
        match(controller: 'article', action: 'update')
        match(controller: 'article', action: 'delete')
        match(controller: 'article', action: 'addAuthors')
        match(controller: 'article', action: 'removeAuthors')
        match(controller: 'author', action: 'create')
        match(controller: 'author', action: 'update')
        match(controller: 'author', action: 'delete')
        match(controller: 'publication', action: 'create')
        match(controller: 'publication', action: 'update')
        match(controller: 'publication', action: 'delete')
        match(controller: 'volume', action: 'create')
        match(controller: 'volume', action: 'update')
        match(controller: 'volume', action: 'delete')
        match(controller: 'volume', action: 'addArticles')
        match(controller: 'volume', action: 'removeArticles')
    }

    @Override
    boolean before() {
        final String token = request.getHeader(AUTHORIZATION_HEADER)?.substring(7)?.trim()
        if (token == null || token.empty) {
            response.status = HttpStatus.FORBIDDEN.value()
            return false
        }
        final def result = JwtService.valid(token)

        if (!result) {
            final def now = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date())
            log.info("[$now] Unauthorized access for route $request.requestURI")
            render(contentType: 'application/json', status: 401, text: '{"message": "Unauthorized"}')
        }
        result
    }
}
