package com.thintwice.archive.mbompay.domain.exception

import com.fasterxml.jackson.annotation.JsonIgnore
import graphql.ExceptionWhileDataFetching


//class SanitizedError(inner: ExceptionWhileDataFetching) : ExceptionWhileDataFetching(inner.exception) {
//    @JsonIgnore
//    override fun getException(): Throwable {
//        return super.getException()
//    }
//}