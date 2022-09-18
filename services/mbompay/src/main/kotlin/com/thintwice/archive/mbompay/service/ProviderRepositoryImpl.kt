package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.domain.common.jsonOf
import com.thintwice.archive.mbompay.domain.input.ProviderInput
import com.thintwice.archive.mbompay.domain.mapper.ProviderMapper
import com.thintwice.archive.mbompay.domain.model.Provider
import com.thintwice.archive.mbompay.repository.ProviderRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.Parameter
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProviderRepositoryImpl(
    private val database: DatabaseClient,
    private val mapper: ProviderMapper,
    private val logger: KLogger = KotlinLogging.logger {},
) : ProviderRepository {
    override suspend fun provider(input: ProviderInput, token: UUID): Optional<Provider> {
        return database.sql("SELECT providers_on_create_edit(input := :input, token := :token)")
            .bind("input", jsonOf(input))
            .bind("token", token)
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun provider(id: UUID, token: UUID): Optional<Provider> {
        return database.sql("SELECT providers_on_find(id := :id, token := :token)")
            .bind("id", id)
            .bind("token", token)
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun deleteProvider(id: UUID, token: UUID): Boolean {
        return database.sql("SELECT providers_on_delete(id := :id, token := :token)")
            .bind("id", id)
            .bind("token", token)
            .fetch()
            .first()
            .map { it.values.firstOrNull() as Boolean? ?: false }
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { false }
    }

    override suspend fun providers(first: Int, after: UUID?, token: UUID): Iterable<Provider> {
        return database.sql("SELECT providers_on_retrieve(first := :first,after := :after, token := :token)")
            .bind("first", first)
            .bind("after", Parameter.fromOrEmpty(after, UUID::class.java))
            .bind("token", token)
            .map(mapper::list)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyList() }
    }

}