package com.pmf.tihedze.repozitorijradova

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class User {
    String username
    String passwordHash

    static mapping = {
        table name: 'users'
        username sqlType: 'varchar', column: 'username'
        passwordHash sqlType: 'varchar', column: 'password_hash'
        version false
    }

    def beforeInsert() {
        encodePassword()
    }

    private def encodePassword() {
        passwordHash = new BCryptPasswordEncoder().encode(passwordHash)
    }
}
