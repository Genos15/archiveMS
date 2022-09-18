package com.thintwice.archive.mbompay.middleware

import com.thintwice.archive.mbompay.domain.common.IMEIValidator
import com.thintwice.archive.mbompay.domain.common.UUIDValidator
import com.thintwice.archive.mbompay.domain.exception.InvalidTokenException
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class SessionMiddleware : WebGraphQlInterceptor {
    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
        // check if we have the imei
        // check if we have the token but without the imei
        // Transform request to token transformed
        val authorizationHeader = request.headers["Authorization"]?.firstOrNull()
        val imeiHeader = request.headers["Imei"]?.firstOrNull()
        val isBearer = authorizationHeader?.lowercase()?.contains("bearer") ?: false
        val isValidImei = IMEIValidator.isValidIMEI(imei = imeiHeader)
        val token: String
        val imei: String
        var isInvalidSession = true
        val contextMap = mutableMapOf<String, Any>()
        if (isBearer || isValidImei) {
            if (isBearer) {
                token = authorizationHeader!!.lowercase().replace("bearer", "").trim()
                isInvalidSession = UUIDValidator.isValidUUID(uuid = token).not()
                if (isInvalidSession.not()) {
                    contextMap["token"] = UUID.fromString(token)
                }
            }

            if (isValidImei) {
                imei = imeiHeader!!.lowercase().trim()
                isInvalidSession = isValidImei.not()
                if (isInvalidSession.not()) {
                    contextMap["imei"] = imei
                }
            }

        }

        if (isInvalidSession) {
            return chain.next(request).map { response ->
                response.transform {
                    it.errors(listOf(InvalidTokenException()))
                }
            }
        }

        request.configureExecutionInput { _, builder ->
            builder.graphQLContext(contextMap).build()
        }

        return chain.next(request)
    }
}