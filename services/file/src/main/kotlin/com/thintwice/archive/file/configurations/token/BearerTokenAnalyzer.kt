package com.thintwice.archive.file.configurations.token

import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import java.util.*

@Component
class BearerTokenAnalyzer : TokenAnalyzer {
    override fun invoke(request: ServerRequest): UUID {
        val full = request.headers().header(HttpHeaders.AUTHORIZATION).firstOrNull()
        return invoke(from = full)
    }

    override fun invoke(from: String?): UUID {
        if (from?.lowercase()?.contains("bearer")?.not() == true) {
            throw RuntimeException("please add a token")
        }
        val token = from?.lowercase()?.replace("bearer ", "", ignoreCase = true)
        return try {
            UUID.fromString(token?.trim())
        } catch (e: Exception) {
            throw RuntimeException("token not found")
        }
    }
}