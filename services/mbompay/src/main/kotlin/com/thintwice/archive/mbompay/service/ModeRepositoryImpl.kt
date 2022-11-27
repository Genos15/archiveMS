package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.configuration.database.DbClient
import com.thintwice.archive.mbompay.domain.common.jsonOf
import com.thintwice.archive.mbompay.domain.common.parameterOrNull
import com.thintwice.archive.mbompay.domain.input.ModeInput
import com.thintwice.archive.mbompay.domain.mapper.CountryMapper
import com.thintwice.archive.mbompay.domain.mapper.ModeMapper
import com.thintwice.archive.mbompay.domain.model.Country
import com.thintwice.archive.mbompay.domain.model.Mode
import com.thintwice.archive.mbompay.repository.ModeRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.util.*

@Service
class ModeRepositoryImpl(
    private val qr: RB,
    private val dbClient: DbClient,
    private val mapper: ModeMapper,
    private val countryMapper: CountryMapper,
    private val logger: KLogger = KotlinLogging.logger {},
) : ModeRepository {

    override suspend fun mode(input: ModeInput, token: UUID): Optional<Mode> {
        val query = qr.l("mutation.create.edit.mode")
        return dbClient.exec(query = query)
            .bind("input", jsonOf(input))
            .bind("token", token)
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun mode(id: UUID, token: UUID): Optional<Mode> {
        val query = qr.l("query.find.mode")
        return dbClient.exec(query = query)
            .bind("id", id)
            .bind("token", token)
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun deleteMode(id: UUID, token: UUID): Boolean {
        val query = qr.l("mutation.delete.mode")
        return dbClient.exec(query = query)
            .bind("id", id)
            .bind("token", token)
            .fetch()
            .first()
            .map { it.values.firstOrNull() as Boolean? ?: false }
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { false }
    }

    override suspend fun modes(first: Int, after: UUID?, token: UUID): Iterable<Mode> {
        val query = qr.l("query.retrieve.mode")
        return dbClient.exec(query = query)
            .bind("first", first)
            .bind("after", parameterOrNull(after))
            .bind("token", token)
            .map(mapper::list)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyList() }
    }

    override suspend fun country(modes: List<Mode>, token: UUID?): Map<Mode, Country> {
        val query = qr.l("query.find.country.mode")
        return Flux.fromIterable(modes)
            .parallel()
            .flatMap { mode ->
                dbClient.exec(query = query)
                    .bind("id", parameterOrNull(mode.id))
                    .bind("token", parameterOrNull(token))
                    .map(countryMapper::factory)
                    .first()
                    .map { AbstractMap.SimpleEntry(mode, it.orElseGet { null }) }
                    .filter { it.value != null }
            }
            .runOn(Schedulers.parallel())
            .sequential()
            .collectList()
            .map { entries -> entries.associateBy({ it.key }, { it.value }) }
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyMap() }
    }

}