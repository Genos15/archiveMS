package com.thintwice.archive.article.configurations.token

import com.thintwice.archive.article.configurations.exceptions.CustomGraphQLError
import graphql.schema.DataFetchingEnvironment
import kotlin.jvm.Throws

interface GeoLocationAnalyzer {
    @Throws(
        RuntimeException::class,
        IllegalArgumentException::class,
        CustomGraphQLError::class,
        NumberFormatException::class
    )
    operator fun invoke(environment: DataFetchingEnvironment): GeoLocationData
}