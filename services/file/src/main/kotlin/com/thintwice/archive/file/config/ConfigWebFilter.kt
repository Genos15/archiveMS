package com.thintwice.archive.file.config

import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class ConfigWebFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        exchange.response
            .headers.add("web-filter", "web-filter-test")
        println(
            """
            =============================================================
            Path: ${exchange.request.path}
            Method: ${exchange.request.method}
            =============================================================
        """.trimIndent()
        )
        return chain.filter(exchange)
    }
}