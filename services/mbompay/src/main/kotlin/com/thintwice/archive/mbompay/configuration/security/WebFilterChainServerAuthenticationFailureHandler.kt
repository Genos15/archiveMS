package com.thintwice.archive.mbompay.configuration.security

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class WebFilterChainServerAuthenticationFailureHandler : ServerAuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        webFilterExchange: WebFilterExchange,
        exception: AuthenticationException?,
    ): Mono<Void> {
        println("WebFilterChainServerAuthenticationFailureHandler in $exception")
        exception?.printStackTrace()
        return Mono.error(exception!!)
    }
}