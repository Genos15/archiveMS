package com.thintwice.archive.mbompay.middleware

import com.thintwice.archive.mbompay.domain.exception.BadRequestException
import graphql.ErrorType
import graphql.GraphQLError
import mu.KLogger
import mu.KotlinLogging
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.stream.Collectors


@Component
class ErrorMiddleware(private val logger: KLogger = KotlinLogging.logger {}) : WebGraphQlInterceptor {
    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
        // check if we have errors
        return chain.next(request).map { response ->
            val graphQLErrors: List<GraphQLError> = response.errors.stream().filter { error ->
                (ErrorType.InvalidSyntax == error.errorType || ErrorType.ValidationError == error.errorType)
            }.map {
                val message = it.message?.split("]")?.last()?.trim { src -> src <= ' ' }
                BadRequestException(errorType = it.errorType, message = message, locations = it.locations)
            }.collect(Collectors.toList())
            if (graphQLErrors.isNotEmpty()) {
                return@map response.transform { builder -> builder.errors(graphQLErrors) }
            }
            logger.info { "\nErrorMiddleware → done" }
            response
        }
    }
}