package com.thintwice.archive.mbompay.middleware

import com.thintwice.archive.mbompay.domain.exception.InvalidTokenException
import com.thintwice.archive.mbompay.domain.model.AuthUser
import org.springframework.dao.DataAccessResourceFailureException
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationMiddleware(private val service: ReactiveUserDetailsService) : WebGraphQlInterceptor {
    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
        val queryLines = request.document.split("\n").filterNot { it.contains("#") }.filter { it.isNotEmpty() }
            .joinToString(separator = "")

        val operationName = readGraphQLDocumentName(document = queryLines)

        val authorizationHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: ""
        val hasAuthorizationHeader =
            authorizationHeader.trim { it <= ' ' }.isNotEmpty() && authorizationHeader.trim { it <= ' ' }
                .startsWith(AUTH_HEADER_VALUE_PREFIX)

        if (hasAuthorizationHeader) {
            val contextMap = mutableMapOf<String, Any>()
            contextMap["token"] = authorizationHeader
            request.configureExecutionInput { _, builder ->
                builder.graphQLContext(contextMap).build()
            }
        }

        if (EXCLUDED_PATHS.contains(operationName)) {
            if (hasAuthorizationHeader) {
                val authentication = UsernamePasswordAuthenticationToken(authorizationHeader, authorizationHeader)
                return chain.next(request).contextWrite(
                    ReactiveSecurityContextHolder.withAuthentication(authentication)
                )
            }
            return chain.next(request)
        } else {
            return Mono.just(authorizationHeader)
                .flatMap { header ->
                    return@flatMap if (hasAuthorizationHeader) {
                        Mono.just(header)
                            .map { it.substring(AUTH_HEADER_VALUE_PREFIX.length) }
                            .flatMap(service::findByUsername)
                            .flatMap {
                                val contextMap = mutableMapOf<String, Any>()
                                contextMap["token"] = it.username
                                request.configureExecutionInput { _, builder ->
                                    builder.graphQLContext(contextMap).build()
                                }
//                        val context: SecurityContext = SecurityContextHolder.createEmptyContext()
                                val authentication: Authentication = if (it is AuthUser) {
                                    UsernamePasswordAuthenticationToken(
                                        it.username, it.password, it.authorities
                                    )
                                } else {
                                    UsernamePasswordAuthenticationToken(it.username, it.password)
                                }
//                        context.authentication = authentication
//                        SecurityContextHolder.setContext(context)
                                chain.next(request).contextWrite(
                                    ReactiveSecurityContextHolder.withAuthentication(authentication)
                                )
                            }.onErrorResume { error ->
                                error.printStackTrace()
                                handleError(error, chain, request)
                            }
                    } else {
                        chain.next(request).map { response ->
                            response.transform {
                                it.errors(listOf(InvalidTokenException()))
                            }
                        }.contextWrite(ReactiveSecurityContextHolder.clearContext())
                    }
                }
        }

    }

    private fun handleError(
        error: Throwable?,
        chain: WebGraphQlInterceptor.Chain,
        request: WebGraphQlRequest,
    ): Mono<WebGraphQlResponse> {
        println("\n\n + in handleError\n\n")

        var cause: String? = null
        if (error is DataAccessResourceFailureException && error.rootCause != null) {
            cause = error.rootCause!!.message
        }
        return chain.next(request).map { response ->
            response.transform {
                it.errors(listOf(InvalidTokenException(cause = cause)))
            }
        }.contextWrite(ReactiveSecurityContextHolder.clearContext())
    }

    private fun readGraphQLDocumentName(document: String): String? {
        return try {
            val documentLocationNameIndex = if (document.split("(").size > 2) 2 else 1
            document.trim { it <= ' ' }
                .replace("{", "∞")
                .replace("(", "∞")
                .replace("__typename", "")
                .split("∞")
                .getOrNull(documentLocationNameIndex)?.trim { it <= ' ' }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private const val AUTH_HEADER_VALUE_PREFIX = "Bearer "
        private const val INTROSPECTION_QUERY_NAME = "IntrospectionQuery"
        private const val SCHEMA_QUERY_NAME = "__schema"
        private const val OTP_QUERY_NAME = "otp"
        private const val CONNECT_QUERY_NAME = "connect"
        private const val REFRESH_QUERY_NAME = "refresh"
        private const val CUSTOMER_MUTATION_NAME = "customer"
        private val EXCLUDED_PATHS = arrayOf(
            INTROSPECTION_QUERY_NAME,
            OTP_QUERY_NAME,
            CONNECT_QUERY_NAME,
            SCHEMA_QUERY_NAME,
            REFRESH_QUERY_NAME,
            CUSTOMER_MUTATION_NAME,
        )
    }
}