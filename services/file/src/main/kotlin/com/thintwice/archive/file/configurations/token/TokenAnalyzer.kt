package com.thintwice.archive.file.configurations.token

import org.springframework.web.reactive.function.server.ServerRequest
import java.util.*
import kotlin.jvm.Throws

interface TokenAnalyzer {
    @Throws(RuntimeException::class, IllegalArgumentException::class)
    operator fun invoke(request: ServerRequest): UUID

    @Throws(RuntimeException::class, IllegalArgumentException::class)
    operator fun invoke(from: String?): UUID
}