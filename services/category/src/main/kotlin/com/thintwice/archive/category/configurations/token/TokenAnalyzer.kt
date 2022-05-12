package com.thintwice.archive.category.configurations.token

import com.thintwice.archive.category.configurations.exceptions.CustomGraphQLError
import graphql.schema.DataFetchingEnvironment
import java.util.*
import kotlin.jvm.Throws

interface TokenAnalyzer {
    @Throws(RuntimeException::class, IllegalArgumentException::class, CustomGraphQLError::class)
    operator fun invoke(environment: DataFetchingEnvironment): UUID
}