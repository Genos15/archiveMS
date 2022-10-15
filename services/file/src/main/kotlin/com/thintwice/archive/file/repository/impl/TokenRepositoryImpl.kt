package com.thintwice.archive.file.repository.impl


import com.thintwice.archive.file.domain.sql_queries.SourceQuery
import com.thintwice.archive.file.repository.TokenRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenRepositoryImpl(
    private val database: DatabaseClient,
    private val logger: KLogger = KotlinLogging.logger {},
) : TokenRepository {

    override suspend fun isActive(token: UUID): Boolean = database.sql(SourceQuery.isActive)
        .bind("token", token)
        .fetch()
        .first()
        .map { it.values.firstOrNull() as Boolean? ?: false }
        .doOnError { logger.error { it.message } }
        .log()
        .awaitFirstOrElse { false }
}