package com.pmf.tihedze.repozitorijradova.services


import com.pmf.tihedze.repozitorijradova.User
import grails.gorm.transactions.Transactional
import org.hibernate.SessionFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import java.security.InvalidParameterException

@Transactional
class UserService {

    void register(String username, String password) {
        def user =  new User(username: username, passwordHash: password)
        user.save(flush: true, insert: true)
    }

    String login(String username, String password) {
        final User user = User.findByUsername(username)
        final bcrypt = new BCryptPasswordEncoder()
        if (!bcrypt.matches(password, user.passwordHash)) {
          throw new InvalidParameterException()
        }
        JwtService.generateJwt(username)
    }
}
