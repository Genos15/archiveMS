package com.thintwice.archive.mbompay.configuration.security

import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono


//@Component
class CustomReactiveAuthManager(userDetailsService: ReactiveUserDetailsService, private val passwordEncoder: PasswordEncoder) :
    UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService) {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        println("CustomReactiveAuthManager -> authenticate")
        println("isAuthenticated -> ${authentication.isAuthenticated}")
        println("credentials -> ${authentication.credentials}")

        val presentedPassword = authentication.credentials as String

        println("presentedPassword -> $presentedPassword")
        println("encodedPassword -> ${passwordEncoder.encode(presentedPassword)}")

        return super
            .authenticate(authentication)
    }
}