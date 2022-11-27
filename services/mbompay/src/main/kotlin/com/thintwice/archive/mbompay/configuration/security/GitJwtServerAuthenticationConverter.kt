package com.thintwice.archive.mbompay.configuration.security

import com.thintwice.archive.mbompay.domain.common.GraphqlRequestResolvableType
import com.thintwice.archive.mbompay.domain.exception.InternalException
import org.springframework.core.ResolvableType
import org.springframework.core.ResolvableType.forClass
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


//@Component
class GitJwtServerAuthenticationConverter(private val service: ReactiveUserDetailsService,
                                          private val passwordEncoder: PasswordEncoder) :
    ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        return readBodyData(exchange)
            .flatMap { it }
            .flatMap { requestBody ->

                println("""
                    -- converter
                    -- operationName = ${requestBody.operationName}
                """.trimIndent())

                return@flatMap if (EXCLUDED_PATHS.contains(requestBody.operationName)) {
                    println("""
                                -- Excluded
                    """.trimIndent())
                    Mono.just(
                        UsernamePasswordAuthenticationToken(
                            "anonymous",
                            passwordEncoder.encode("anonymous"),
                            AuthorityUtils.createAuthorityList("ANONYMOUS")
                        )
                    )
                } else {
                    val authorizationHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)?:""
                    Mono.just(authorizationHeader)
                        .flatMap { header ->
                            val hasAuthorizationHeader = header.trim { it <= ' ' }
                                .isNotEmpty() && header.trim { it <= ' ' }
                                .startsWith(AUTH_HEADER_VALUE_PREFIX)
                            println("""
                                -- hasAuthorizationHeader = $hasAuthorizationHeader
                            """.trimIndent())
                            if (hasAuthorizationHeader) {
                                Mono.just(header)
                                    .map { it.substring(AUTH_HEADER_VALUE_PREFIX.length) }
                                    .flatMap(service::findByUsername)
                                    .map { user ->
                                        UsernamePasswordAuthenticationToken(
                                            user,
                                            passwordEncoder.encode(user.password)
                                        )
                                    }
                            } else {
                                Mono.error(InternalException("invalid token"))
                            }
                        }
                }
            }
    }

    private fun readBodyData(exchange: ServerWebExchange) = exchange.request.body.map { body ->
        val decoder = Jackson2JsonDecoder()
        val elementType: ResolvableType = forClass(GraphqlRequestResolvableType::class.java)
        return@map decoder.decodeToMono(
            Mono.just(body), elementType, MediaType.APPLICATION_GRAPHQL, emptyMap()
        ).cast(GraphqlRequestResolvableType::class.java)
    }.next()

    companion object {
        private const val AUTH_HEADER_VALUE_PREFIX = "Bearer "
        private const val INTROSPECTION_QUERY_NAME = "IntrospectionQuery"
        private const val OTP_QUERY_NAME = "otp"
        private const val CONNECT_QUERY_NAME = "connect"
        private val EXCLUDED_PATHS = arrayOf(INTROSPECTION_QUERY_NAME, OTP_QUERY_NAME, CONNECT_QUERY_NAME)
    }
}