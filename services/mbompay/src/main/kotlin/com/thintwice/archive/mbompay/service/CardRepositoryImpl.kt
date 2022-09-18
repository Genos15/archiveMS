package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.domain.common.jsonOf
import com.thintwice.archive.mbompay.domain.input.CardInput
import com.thintwice.archive.mbompay.domain.mapper.CardMapper
import com.thintwice.archive.mbompay.domain.mapper.ProviderMapper
import com.thintwice.archive.mbompay.domain.model.Card
import com.thintwice.archive.mbompay.domain.model.Provider
import com.thintwice.archive.mbompay.repository.CardRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.r2dbc.core.Parameter
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.util.*

@Service
class CardRepositoryImpl(
    private val database: DatabaseClient,
    private val mapper: CardMapper,
    private val providerMapper: ProviderMapper,
    private val logger: KLogger = KotlinLogging.logger {},
) : CardRepository {
    override suspend fun card(input: CardInput, token: UUID): Optional<Card> {
        return database.sql("SELECT cards_on_create_edit(input := :input, token := :token)")
            .bind("input", jsonOf(input))
            .bind("token", token)
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun card(id: UUID, token: UUID): Optional<Card> {
        return database.sql("SELECT cards_on_find(id := :id, token := :token)")
            .bind("id", id)
            .bind("token", token)
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun deleteCard(id: UUID, token: UUID): Boolean {
        return database.sql("SELECT cards_on_delete(id := :id, token := :token)")
            .bind("id", id)
            .bind("token", token)
            .fetch()
            .first()
            .map { it.values.firstOrNull() as Boolean? ?: false }
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { false }
    }

    override suspend fun cards(first: Int, after: UUID?, token: UUID): Iterable<Card> {
        return database.sql("SELECT cards_on_retrieve(first := :first, after := :after, token := :token)")
            .bind("first", first)
            .bind("after", Parameter.fromOrEmpty(after, UUID::class.java))
            .bind("token", token)
            .map(mapper::list)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyList() }
    }

    override suspend fun providers(cards: List<Card>, token: UUID?): Map<Card, Provider> {
        return Flux.fromIterable(cards)
            .parallel()
            .flatMap { card ->
                database.sql("SELECT cards_on_retrieve_provider(id := :id, token := :token)")
                    .bind("id", Parameter.from(card.id))
                    .bind("token", Parameter.fromOrEmpty(token, UUID::class.java))
                    .map(providerMapper::factory)
                    .first()
                    .map { AbstractMap.SimpleEntry(card, it.orElseGet { null }) }
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