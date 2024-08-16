package com.pmf.tihedze.repozitorijradova.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import groovy.transform.CompileStatic

@CompileStatic
class JwtService {
    private static int HOUR = 3600 * 1000
    private static String secret = 'secret'

    static String generateJwt(String username) {
        final Date now = new Date()
        JWT.create()
            .withIssuer('repozitorij-radova')
            .withClaim('username', username)
            .withExpiresAt(new Date(now.getTime() + 24 * HOUR))
            .sign(Algorithm.HMAC256(secret))
    }

    static boolean valid(String token) {
        try {
            final def verifier = JWT.require(Algorithm.HMAC256(secret)).build()
            def jwt = verifier.verify(token);
            jwt.issuer == 'repozitorij-radova'
        } catch(Exception ignored) {
            false
        }
    }
}
