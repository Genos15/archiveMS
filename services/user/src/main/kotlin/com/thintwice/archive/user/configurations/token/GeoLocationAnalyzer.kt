package com.thintwice.archive.user.configurations.token

import com.thintwice.archive.user.configurations.exceptions.CustomGraphQLError
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