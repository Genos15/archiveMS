package com.thintwice.archive.mbompay.service

import com.stripe.model.Card
import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.configuration.database.DbClient
import com.thintwice.archive.mbompay.configuration.security.CryptoAES
import com.thintwice.archive.mbompay.domain.common.jsonOf
import com.thintwice.archive.mbompay.domain.input.CardInput
import com.thintwice.archive.mbompay.domain.input.JCardInput
import com.thintwice.archive.mbompay.domain.input.UpdateCardInput
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
    private val crypto: CryptoAES,
) : CardRepository {

    override suspend fun create(input: CardInput, token: String): Optional<JCard> {
        val customer = customerFactory.activeCustomer(accessToken = token)
        val stripeCard = cardFactory.create(customer = customer, card = input)
            ?: return Optional.empty()

        val currency = cardFactory.currencyByCountry(country = stripeCard.country)
        val inputStripeCard = JCardInput(stripeCard = stripeCard, currency = currency)
        return create(input = inputStripeCard, token = token)
    }

    override suspend fun create(input: String, token: String): String? {
        val decryptedInput = crypto.decrypt(encrypted = input) ?: return null
        val formattedInput = mapper.input(source = decryptedInput) ?: return null
        val optionalCard = create(input = formattedInput, token = token)
        if (optionalCard.isPresent.not()) {
            return null
        }

        val outcomeJson = jsonOf(element = optionalCard.get()).asString()
        return crypto.encrypt(source = outcomeJson)
    }

    override suspend fun update(input: String, token: String): String? {
        val decryptedInput = crypto.decrypt(encrypted = input) ?: return null
        val formattedInput = mapper.inputIssuer(source = decryptedInput) ?: return null
        println(formattedInput)

        val dbCard = find(id = formattedInput.id, token = token)
        if (dbCard.isPresent.not()) {
            return null
        }

        val cardToken = cardFactory.updateIssue(card = dbCard.get(), input = formattedInput)

        println(dbCard)
        println(cardToken)

        return null
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

    override suspend fun defaultCard(id: UUID, token: String): Optional<JCard> {
        val input = UpdateCardInput(id = id, isDefault = true)
        val query = qr.l("mutation.create.edit.card")
        return dbClient.exec(query = query)
            .bind("input", jsonOf(input))
            .bind("token", token)
            .map(mapper::factory)
            .first()
            .awaitFirstOrDefault(Optional.empty())
    }

    override suspend fun stripeEventCreate(card: Card): Boolean {
        //TODO: create a card if not create in the database

        val currency = cardFactory.currencyByCountry(country = card.country)
        val inputStripeCard = JCardInput(stripeCard = card, currency = currency)
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