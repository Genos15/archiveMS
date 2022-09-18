package com.thintwice.archive.mbompay.domain.exception

import graphql.ErrorClassification
import graphql.GraphQLError
import graphql.language.SourceLocation

class BadRequestException(
    private val errorType: ErrorClassification,
    private val message: String?,
    private val locations: MutableList<SourceLocation>? = null,
) : GraphQLError {
    override fun getMessage(): String = message ?: "Bad Request"

    override fun getLocations(): MutableList<SourceLocation> = locations ?: mutableListOf()

    override fun getErrorType(): ErrorClassification = errorType
}