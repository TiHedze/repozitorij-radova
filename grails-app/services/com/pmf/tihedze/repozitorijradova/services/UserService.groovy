package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Publication
import com.pmf.tihedze.repozitorijradova.User
import com.pmf.tihedze.repozitorijradova.exceptions.PublicationNotFoundException
import groovy.transform.CompileStatic
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import java.security.InvalidParameterException

class UserService {

    void register(String username, String password, String publicationName) {
        def publication = Publication.findByName(publicationName)
        if (publication == null) {
            throw new PublicationNotFoundException("No publication with that name found")
        }
        def user =  new User(username: username, passwordHash: password, publication: publication)
        user.save(flush: true)
    }

    String login(String username, String password) {
        final User user = User.findByUsername(username)
        final bcrypt = new BCryptPasswordEncoder()
        if (!bcrypt.matches(password, user.passwordHash)) {
          throw new InvalidParameterException()
        }
        JwtService.generateJwt(user, username)
    }
}
