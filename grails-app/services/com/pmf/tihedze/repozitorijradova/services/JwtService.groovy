package com.pmf.tihedze.repozitorijradova.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.pmf.tihedze.repozitorijradova.User
import groovy.transform.CompileStatic

import java.security.Key
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec

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

    static DecodedJWT decodeJwt(String token) {
        try {
            final def verifier = JWT.require(Algorithm.HMAC256(secret)).build()
            verifier.verify(token);
        } catch(Exception ignored) {
            return null
        }
    }
}
