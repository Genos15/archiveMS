package com.thintwice.archive.mbompay.configuration.errors

import com.thintwice.archive.mbompay.domain.exception.BadRequestException
import com.thintwice.archive.mbompay.domain.exception.NotFoundException
import graphql.ErrorType
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import mu.KLogger
import mu.KotlinLogging
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.stereotype.Component

@Component
class GraphQLExceptionHandler(
    private val logger: KLogger = KotlinLogging.logger {},
) : DataFetcherExceptionResolverAdapter() {

    override fun resolveToSingleError(e: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        logger.error { "\nGraphQLExceptionHandler → ${e.message}" }
        return when (e) {
            is NotFoundException -> toGraphQLError(e)
            is Exception -> BadRequestException(message = e.message)
            else -> super.resolveToSingleError(e, env)
        }
    }

    private fun toGraphQLError(e: Throwable): GraphQLError {
        return GraphqlErrorBuilder.newError().message(e.message).errorType(ErrorType.DataFetchingException).build()
    }
}
