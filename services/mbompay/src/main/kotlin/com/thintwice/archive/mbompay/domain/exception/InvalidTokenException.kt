package com.thintwice.archive.mbompay.domain.exception

import com.thintwice.archive.mbompay.domain.exception.InvalidTokenException.State.*
import graphql.ErrorClassification
import graphql.ErrorType
import graphql.GraphQLError
import graphql.language.SourceLocation

class InvalidTokenException(private val cause: String? = null, private val state: State = UNKNOWN) : GraphQLError {
    override fun getMessage(): String = when {
        cause != null -> cause
        state == UNKNOWN -> "something went wrong"
        state == INVALID -> "invalid token"
        state == EXPIRED -> "token is expired"
        else -> "something went wrong!"
    }

    override fun getLocations(): MutableList<SourceLocation> = mutableListOf()

    override fun getErrorType(): ErrorClassification = ErrorType.ValidationError

    enum class State {
        INVALID,
        UNKNOWN,
        EXPIRED
    }
}