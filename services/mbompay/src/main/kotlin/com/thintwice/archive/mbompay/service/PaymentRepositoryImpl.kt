package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.configuration.database.DbClient
import com.thintwice.archive.mbompay.domain.common.jsonOf
import com.thintwice.archive.mbompay.domain.common.parameterOrNull
import com.thintwice.archive.mbompay.domain.input.PaymentIntentInput
import com.thintwice.archive.mbompay.domain.mapper.JCardMapper
import com.thintwice.archive.mbompay.domain.mapper.JTransactionMapper
import com.thintwice.archive.mbompay.domain.model.JCard
import com.thintwice.archive.mbompay.domain.model.JTransaction
import com.thintwice.archive.mbompay.repository.PaymentRepository
import kotlinx.coroutines.reactive.awaitFirstOrDefault
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.time.OffsetDateTime
import java.util.*

@Service
class PaymentRepositoryImpl(
    private val qr: RB,
    private val mapper: JTransactionMapper,
    private val cardMapper: JCardMapper,
    private val dbClient: DbClient,
) : PaymentRepository {
    override suspend fun update(input: PaymentIntentInput, accessToken: String?): Optional<JTransaction> {
        val query = qr.l("mutation.create.edit.transaction")
        return dbClient.exec(query = query)
            .bind("input", jsonOf(input))
            .map(mapper::factory)
            .first()
            .awaitFirstOrDefault(Optional.empty())
    }

    override suspend fun list(first: Long, after: UUID?, accessToken: String): Iterable<JTransaction> {
        val query = qr.l("query.retrieve.transaction")
        return dbClient.exec(query = query)
            .bind("first", first)
            .bind("after", parameterOrNull(after))
            .bind("access_token", accessToken)
            .map(mapper::list)
            .first()
            .awaitFirstOrDefault(emptyList())
    }

    override suspend fun list(
        fromDate: OffsetDateTime,
        toDate: OffsetDateTime,
        accessToken: String,
    ): Iterable<JTransaction> {
        val query = qr.l("query.retrieve.transaction.interval")
        return dbClient.exec(query = query)
            .bind("from_date", fromDate)
            .bind("to_date", toDate)
            .bind("access_token", accessToken)
            .map(mapper::list)
            .first()
            .awaitFirstOrDefault(emptyList())
    }

    override suspend fun card(transactions: List<JTransaction>): Map<JTransaction, JCard> {
        val query = qr.l("query.retrieve.transaction.card")
        return Flux.fromIterable(transactions)
            .parallel()
            .flatMap { transaction ->
                dbClient.exec(query = query)
                    .bind("id", transaction.id)
                    .map(cardMapper::factory)
                    .first()
                    .map { AbstractMap.SimpleEntry(transaction, it.orElseGet { null }) }
                    .filter { it.value != null }
            }
            .runOn(Schedulers.parallel())
            .sequential()
            .collectList()
            .map { entries -> entries.associateBy({ it.key }, { it.value }) }
            .awaitFirstOrElse { emptyMap() }
    }

}