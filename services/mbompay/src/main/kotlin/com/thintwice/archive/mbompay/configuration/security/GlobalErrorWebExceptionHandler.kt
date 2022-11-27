package com.thintwice.archive.mbompay.configuration.security

import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*

import reactor.core.publisher.Mono


//@Component
//@Order(-2)
class GlobalErrorWebExceptionHandler(
    errorAttributes: ErrorAttributes,
    resources: WebProperties.Resources,
    applicationContext: ApplicationContext,
) : AbstractErrorWebExceptionHandler(errorAttributes, resources, applicationContext) {
    override fun getRoutingFunction(
        errorAttributes: ErrorAttributes?,
    ): RouterFunction<ServerResponse> {
        return RouterFunctions.route(
            RequestPredicates.all()
        ) { request: ServerRequest -> renderErrorResponse(request) }
    }

    private fun renderErrorResponse(
        request: ServerRequest,
    ): Mono<ServerResponse> {
        val errorPropertiesMap: Map<String, Any> = getErrorAttributes(
            request, ErrorAttributeOptions.defaults()
        )
        println("in GlobalErrorWebExceptionHandler")
        return ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(errorPropertiesMap))
    }
}