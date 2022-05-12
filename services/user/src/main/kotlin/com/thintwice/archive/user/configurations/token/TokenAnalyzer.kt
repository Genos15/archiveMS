package com.thintwice.archive.user.configurations.token

import com.thintwice.archive.user.configurations.exceptions.CustomGraphQLError
import graphql.schema.DataFetchingEnvironment
import java.util.*
import kotlin.jvm.Throws

interface TokenAnalyzer {
    @Throws(RuntimeException::class, IllegalArgumentException::class, CustomGraphQLError::class)
    operator fun invoke(environment: DataFetchingEnvironment): UUID
}