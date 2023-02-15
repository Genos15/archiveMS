package com.thintwice.archive.mbompay.service

import com.stripe.model.Card
import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.configuration.database.DbClient
import com.thintwice.archive.mbompay.domain.common.jsonOf
import com.thintwice.archive.mbompay.domain.input.CardInput
import com.thintwice.archive.mbompay.domain.input.JCardInput
import com.thintwice.archive.mbompay.domain.mapper.JCardMapper
import com.thintwice.archive.mbompay.domain.model.JCard
import com.thintwice.archive.mbompay.repository.CardRepository
import com.thintwice.archive.mbompay.repository.StripeCardRepository
import com.thintwice.archive.mbompay.repository.StripeCustomerRepository
import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.springframework.r2dbc.core.Parameter
import org.springframework.stereotype.Service
import java.util.*

@Service
class CardRepositoryImpl(
    private val customerFactory: StripeCustomerRepository,
    private val cardFactory: StripeCardRepository,
    private val qr: RB,
    private val dbClient: DbClient,
    private val mapper: JCardMapper,
) : CardRepository {

    override suspend fun create(input: CardInput, token: String): Optional<JCard> {
        val customer = customerFactory.activeCustomer(accessToken = token)
        val stripeCard = cardFactory.create(customer = customer, card = input)
            ?: return Optional.empty()

        val inputStripeCard = JCardInput(stripeCard = stripeCard)
        return create(input = inputStripeCard, token = token)
    }

    private suspend fun create(input: JCardInput, token: String): Optional<JCard> {
        val query = qr.l("mutation.create.edit.card")
        return dbClient.exec(query = query)
            .bind("input", jsonOf(input))
            .bind("token", token)
            .map(mapper::factory)
            .first()
            .awaitFirstOrDefault(Optional.empty())
    }

    override suspend fun find(id: UUID, token: String): Optional<JCard> {
        val query = qr.l("query.find.card")
        return dbClient.exec(query = query)
            .bind("id", id)
            .map(mapper::factory)
            .first()
            .awaitFirstOrDefault(Optional.empty())
    }

    override suspend fun stripeEventCreate(card: Card): Boolean {
        //TODO: create a card if not create in the database
        val inputStripeCard = JCardInput(stripeCard = card)
        val result = create(input = inputStripeCard, token = "")
        return result.isPresent
    }

    override suspend fun delete(id: UUID, token: String): Boolean {
        val customer = customerFactory.activeCustomer(accessToken = token)

        val dbCard = find(id = id, token = token)

        if (dbCard.isPresent.not() || dbCard.get().providerId == null) {
            return false
        }

        val query = qr.l("mutation.delete.card")

        val dbDeleted = dbClient.exec(query = query)
            .bind("id", id)
            .fetch()
            .first()
            .map { it.values.firstOrNull() as Boolean? ?: false }
            .awaitFirstOrDefault(false)

        if (dbDeleted == false) {
            return false
        }

        val stripeDeleted = cardFactory.delete(customer = customer, cardId = dbCard.get().providerId!!)

        return dbDeleted && stripeDeleted
    }

    override suspend fun stripeEventDelete(card: Card): Boolean {
        val query = qr.l("mutation.delete.card.by.provider.id")
        return dbClient.exec(query = query)
            .bind("id", card.id)
            .fetch()
            .first()
            .map { it.values.firstOrNull() as Boolean? ?: false }
            .awaitFirstOrDefault(false)
    }

    override suspend fun cards(first: Long, after: UUID?, token: String): Iterable<JCard> {
        val query = qr.l("query.retrieve.card")
        return dbClient.exec(query = query)
            .bind("first", first)
            .bind("after", Parameter.fromOrEmpty(after, UUID::class.java))
            .bind("access_token", token)
            .map(mapper::list)
            .first()
            .awaitFirstOrDefault(emptyList())
    }
}