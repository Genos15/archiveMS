package com.thintwice.archive.mbompay.middleware

import com.thintwice.archive.mbompay.middleware.implementation.*
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationMiddleware(private val service: ReactiveUserDetailsService) : WebGraphQlInterceptor {
    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {

        val nameReader = GraphQLDocumentName(request = request)
        val operationName = nameReader()

        return when {
            kEXCLUDED_PATHS.contains(operationName) -> {
                val noAuthHandler = NoAuthHandler(request = request, chain = chain)
                noAuthHandler()
            }

            kFIREBASE_SUPPORT_AUTH_PATHS.contains(operationName) -> {
                val firebaseAuthHandler = FirebaseAuthHandler(request = request, chain = chain)
                firebaseAuthHandler()
            }

            else -> {
                val jesendAuthHandler = JesendAuthHandler(request = request, chain = chain, service = service)
                jesendAuthHandler()
            }
        }
    }

    companion object {
        private const val INTROSPECTION_QUERY_NAME = "IntrospectionQuery"
        private const val SCHEMA_QUERY_NAME = "__schema"
        private const val OTP_QUERY_NAME = "otp"
        private const val CONNECT_QUERY_NAME = "connect"
        private const val REFRESH_QUERY_NAME = "refresh"
        private const val CUSTOMER_MUTATION_NAME = "customer"
        private val kEXCLUDED_PATHS = arrayOf(
            INTROSPECTION_QUERY_NAME,
            OTP_QUERY_NAME,
            CONNECT_QUERY_NAME,
            SCHEMA_QUERY_NAME,
            REFRESH_QUERY_NAME,
        )
        private val kFIREBASE_SUPPORT_AUTH_PATHS = arrayOf(
            CUSTOMER_MUTATION_NAME,
        )
    }
}