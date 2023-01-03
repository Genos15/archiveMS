package com.thintwice.archive.mbompay.middleware.implementation

import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import reactor.core.publisher.Mono

class NoAuthHandler(
    private val request: WebGraphQlRequest,
    private val chain: WebGraphQlInterceptor.Chain,
) {
    operator fun invoke(): Mono<WebGraphQlResponse> {
        val authHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        return if (authHeader != null) {
            val authentication = UsernamePasswordAuthenticationToken(authHeader, authHeader)
            chain.next(request).contextWrite(
                ReactiveSecurityContextHolder.withAuthentication(authentication)
            )
        } else {
            chain.next(request)
        }
    }
}