package com.thintwice.archive.mbompay.middleware

import com.thintwice.archive.mbompay.domain.exception.BadRequestException
import com.thintwice.archive.mbompay.domain.exception.NotFoundException
import graphql.ErrorType
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.core.annotation.Order
import org.springframework.dao.DataAccessResourceFailureException
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.stereotype.Component
import java.util.*

@Order(-2)
@Component
class ErrorMiddlewareRuntime: DataFetcherExceptionResolverAdapter() {
    override fun resolveToSingleError(e: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        println("""
            -- ErrorMiddlewareRuntime
            -- message = ${e.message}
        """.trimIndent())
        e.printStackTrace()
        return when (e) {
            is NotFoundException -> toGraphQLError(e)
            is DataAccessResourceFailureException -> BadRequestException(message = e.message)
            is Exception -> BadRequestException(message = e.message)
            else -> super.resolveToSingleError(e, env)
        }
    }

    private fun toGraphQLError(e: Throwable): GraphQLError {
        return GraphqlErrorBuilder.newError().message(e.message).errorType(ErrorType.DataFetchingException).build()
    }
}