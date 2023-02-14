package com.thintwice.archive.mbompay.service

import com.stripe.Stripe
import com.stripe.model.Card
import com.stripe.model.Customer
import com.stripe.model.Token
import com.stripe.param.CardUpdateOnCustomerParams
import com.stripe.param.PaymentSourceCollectionListParams
import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.domain.input.CardInput
import com.thintwice.archive.mbompay.repository.StripeCardRepository
import org.springframework.stereotype.Service
import java.util.*


@Service
class StripeCardRepositoryImpl(sr: RB) : StripeCardRepository {

    private val secretApiKey = sr.l("stripe.secret.api.key")

    override suspend fun create(customer: Customer, card: CardInput): Card? {
        Stripe.apiKey = secretApiKey
        val tokenCard = Token.create(card.createToken())
        val source = card.createSource(tokenCard.id)
        return customer.sources.create(source) as? Card?
    }

    override suspend fun update(customer: Customer, cardId: String, card: CardInput): Card {
        CardUpdateOnCustomerParams.builder()

            .build()
        TODO("Not yet implemented")
    }

    override suspend fun delete(customer: Customer, cardId: String): Boolean {
        Stripe.apiKey = secretApiKey
        val card = customer.sources.retrieve(cardId) as? Card?
        card?.update(CardUpdateOnCustomerParams.builder().build())
        val deletedCard = card?.delete()
        return deletedCard?.deleted == true
    }

    override suspend fun find(customer: Customer, cardId: String): Optional<Card> {
        Stripe.apiKey = secretApiKey
        val card = customer.sources.retrieve(cardId) as? Card?
        return Optional.ofNullable(card)
    }

    override suspend fun retrieve(first: Long, customer: Customer): Iterable<Card> {
        Stripe.apiKey = secretApiKey
        val params = PaymentSourceCollectionListParams.builder()
            .setLimit(first)
            .setObject("card")
            .build()

        val cards = customer.sources.list(params)
        return cards.data.filterIsInstance<Card>()
    }


//    suspend fun cards(first: Long, after: UUID?, token: String): Iterable<JCard> {
//        val customer = customerFactory.activeCustomer(accessToken = token)
//        val stripCards = cardFactory.retrieve(customer = customer, first = first)
//        return stripCards.map { JCard(stripeCard = it) }
//    }
}