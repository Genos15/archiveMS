package com.thintwice.archive.mbompay.configuration.security

import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

//@Component
class CustomReactiveAuthManager(userDetailsService: ReactiveUserDetailsService, private val passwordEncoder: PasswordEncoder,) :
    UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService) {

    private val anonymousAuthority = AuthorityUtils.createAuthorityList("ANONYMOUS").first()
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        setPasswordEncoder(passwordEncoder)
        return super.authenticate(authentication)
    }
}