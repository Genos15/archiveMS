package com.thintwice.archive.mbompay.configuration.security

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class JWTReactiveAuthorizationFilter(private val jwtService: JWTService) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: return chain.filter(exchange)

        if (!authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange)
        }

        try {
            val token = jwtService.decodeAccessToken(authHeader)
            println("JWTReactiveAuthorizationFilter -> 1")
            println("JWTReactiveAuthorizationFilter -> ${token.subject}")
            println("JWTReactiveAuthorizationFilter -> 2")
            println("JWTReactiveAuthorizationFilter -> ${jwtService.getRoles(token)}")
            println("JWTReactiveAuthorizationFilter -> 3")
            val auth = UsernamePasswordAuthenticationToken(token.subject, null, jwtService.getRoles(token))
            println("JWTReactiveAuthorizationFilter -> 4")
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
        } catch (e: Exception) {
            println("JWT exception $e")
        }

        return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.clearContext())
    }
}