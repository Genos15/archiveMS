package com.thintwice.archive.mbompay.middleware

import com.thintwice.archive.mbompay.domain.input.LocalizationInput
import mu.KLogger
import mu.KotlinLogging
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class LocalizationMiddleware(private val logger: KLogger = KotlinLogging.logger {}) : WebGraphQlInterceptor {
    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
        // check if we have the longitude, latitude and the language
        val latitudeHeader = request.headers["Latitude"]?.firstOrNull()
        val longitudeHeader = request.headers["Longitude"]?.firstOrNull()
        val contextMap = mutableMapOf<String, Any>()
        val xLong = longitudeHeader?.toFloatOrNull()
        val xLat = latitudeHeader?.toFloatOrNull()
        val language = request.locale?.language
        contextMap["where"] = LocalizationInput(latitude = xLat, longitude = xLong, language = language)
        request.configureExecutionInput { _, builder ->
            builder.graphQLContext(contextMap).build()
        }
        logger.info { "\nLocalizationMiddleware → done" }
        return chain.next(request)
    }
}