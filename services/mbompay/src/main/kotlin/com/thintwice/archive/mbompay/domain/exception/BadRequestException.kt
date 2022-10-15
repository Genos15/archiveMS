package com.thintwice.archive.mbompay.domain.exception

import graphql.ErrorClassification
import graphql.GraphQLError
import graphql.language.SourceLocation
import org.springframework.graphql.execution.ErrorType

class BadRequestException(
    private val errorType: ErrorClassification = ErrorType.BAD_REQUEST,
    private val message: String?,
    private val locations: MutableList<SourceLocation>? = null,
) : GraphQLError {
    override fun getMessage(): String = message ?: "Bad Request"

    override fun getLocations(): MutableList<SourceLocation> = locations ?: mutableListOf()

    override fun getErrorType(): ErrorClassification = errorType
}