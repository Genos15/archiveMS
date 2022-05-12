package com.thintwice.archive.auth.repository.impl

import com.thintwice.archive.auth.domain.input.TokenInput
import com.thintwice.archive.auth.domain.mapper.TokenMapper
import com.thintwice.archive.auth.domain.model.Token
import com.thintwice.archive.auth.domain.sql_queries.SourceQuery
import com.thintwice.archive.auth.repository.TokenRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenRepositoryImpl(
    private val database: DatabaseClient,
    private val mapper: TokenMapper,
    private val logger: KLogger = KotlinLogging.logger {},
) : TokenRepository {

    override suspend fun login(input: TokenInput): Optional<Token> = database.sql(SourceQuery.create)
        .bind("input", input.toJson())
        .map(mapper::asSingle)
        .first()
        .doOnError { logger.error { it.message } }
        .log()
        .awaitFirstOrElse { Optional.empty() }

    override suspend fun refresh(input: UUID): Optional<Token> = database.sql(SourceQuery.refresh)
        .bind("input", input)
        .map(mapper::asSingle)
        .first()
        .doOnError { logger.error { it.message } }
        .log()
        .awaitFirstOrElse { Optional.empty() }
}