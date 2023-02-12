package com.thintwice.archive.mbompay.middleware.implementation

import com.thintwice.archive.mbompay.domain.model.AuthUser
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import reactor.core.publisher.Mono
import kotlin.streams.toList


class JesendAuthHandler(
    private val request: WebGraphQlRequest,
    private val chain: WebGraphQlInterceptor.Chain,
    private val service: ReactiveUserDetailsService,
) {

    operator fun invoke(): Mono<WebGraphQlResponse> {
        val authHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        return if (hasAuthHeader(authHeader)) {
            Mono.just(authHeader!!.substring(AUTH_HEADER_VALUE_PREFIX.length))
                .flatMap(service::findByUsername)
                .flatMap {
                    val contextMap = mutableMapOf<String, Any>()
                    contextMap["token"] = it.username
                    request.configureExecutionInput { _, builder ->
                        builder.graphQLContext(contextMap).build()
                    }
                    val authentication: Authentication = if (it is AuthUser) {
                        UsernamePasswordAuthenticationToken(
                            it.username, it.password, it.authorities
                        )
                    } else {
                        UsernamePasswordAuthenticationToken(it.username, it.password)
                    }
                    chain.next(request).contextWrite(
                        ReactiveSecurityContextHolder.withAuthentication(authentication)
                    )
                }.onErrorResume { error ->
                    println("-- JesendAuthHandler ${error.message}")
                    onError(chain = chain, request = request)
                }
        } else {
            chain.next(request).map(this::processResponse).contextWrite(ReactiveSecurityContextHolder.clearContext())
        }
    }

    private fun hasAuthHeader(authHeader: String?): Boolean {
        return if (authHeader == null) {
            false
        } else {
            authHeader.trim { it <= ' ' }.isNotEmpty() && authHeader.trim { it <= ' ' }
                .startsWith(AUTH_HEADER_VALUE_PREFIX)
        }
    }

    private fun onError(
        chain: WebGraphQlInterceptor.Chain,
        request: WebGraphQlRequest,
    ): Mono<WebGraphQlResponse> {
        println("-- handler error JesendAuthHandler --")
        return chain.next(request).map(this::processResponse).contextWrite(ReactiveSecurityContextHolder.clearContext())
    }

    private fun processResponse(response: WebGraphQlResponse): WebGraphQlResponse {
        return if (response.isValid) {
            response
        } else {
            val errors: List<GraphQLError> = response.errors.stream().map { error ->
                val builder = GraphqlErrorBuilder.newError()
                println("-- chain error $error")
                builder.build()
            }.toList()

            response.transform { builder ->
                builder.errors(errors).build()
            }
        }
    }

    companion object {
        private const val AUTH_HEADER_VALUE_PREFIX = "Bearer "
    }
}