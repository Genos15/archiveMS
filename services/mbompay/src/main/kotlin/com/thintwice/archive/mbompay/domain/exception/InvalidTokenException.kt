package com.thintwice.archive.mbompay.domain.exception

import graphql.ErrorClassification
import graphql.ErrorType
import graphql.GraphQLError
import graphql.language.SourceLocation

class InvalidTokenException : GraphQLError {
    override fun getMessage(): String = "invalid token"

    override fun getLocations(): MutableList<SourceLocation> = mutableListOf()

    override fun getErrorType(): ErrorClassification = ErrorType.ValidationError
}