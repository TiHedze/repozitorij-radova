package com.pmf.tihedze.repozitorijradova.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import groovy.time.TimeCategory
import groovy.transform.CompileStatic

class JwtService {
    private static String secret = 'secret'

    static String generateJwt(String username) {
        use(TimeCategory) {
            JWT.create()
                    .withIssuer('repozitorij-radova')
                    .withClaim('username', username)
                    .withExpiresAt(new Date() + 1.day)
                    .sign(Algorithm.HMAC256(secret))
        }
    }

    static boolean valid(String token) {
        try {
            final def verifier = JWT.require(Algorithm.HMAC256(secret)).build()
            def jwt = verifier.verify(token);
            jwt.issuer == 'repozitorij-radova' && jwt.expiresAt > new Date()
        } catch(Exception ignored) {
            false
        }
    }
}
