package com.thintwice.archive.mbompay.service

import com.stripe.Stripe
import com.stripe.model.Card
import com.stripe.model.Charge
import com.stripe.model.Token
import com.stripe.param.ChargeCreateParams
import com.stripe.param.PaymentSourceCollectionListParams
import com.stripe.param.TokenCreateParams
import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.repository.StripeChargeRepository
import com.thintwice.archive.mbompay.repository.StripeCustomerRepository
import org.springframework.stereotype.Service


@Service
class StripeChargeRepositoryImpl(
    sr: RB, private val customerFactory: StripeCustomerRepository,
) : StripeChargeRepository {

    private val secretApiKey = sr.l("stripe.secret.api.key")
    override suspend fun send(amount: Long, token: String): Charge? {
        Stripe.apiKey = secretApiKey
        val customer = customerFactory.activeCustomer(accessToken = token)
        println("-- has gotten customer")
        val sourceParams = PaymentSourceCollectionListParams
            .builder()
            .setLimit(1)
            .build()

        val customerCards = customer.sources.list(sourceParams)
        val defaultCard = customerCards.data.filterIsInstance<Card>().firstOrNull()
        println("-- getting card")

        if (customerCards == null || defaultCard == null) {
            throw RuntimeException("no payment method registered")
        }
        println("-- has gotten card")

        val chargeSource = ChargeCreateParams.builder()
            .setAmount(amount)
            .setCurrency("eur")
            .setCustomer(customer.id)
//            .setApplicationFeeAmount(2_000)
            .setSource(defaultCard.id)
            .build()
        println("-- has created charge")
        return Charge.create(chargeSource)
    }

    override suspend fun refund(): Charge? {
        Stripe.apiKey = secretApiKey
        TODO("Not yet implemented")
    }

    override suspend fun retrieve(first: Long): Iterable<Charge> {
        Stripe.apiKey = secretApiKey
        TODO("Not yet implemented")
    }

//    override suspend fun create(customer: Customer, card: CardInput): Card? {
//        Stripe.apiKey = secretApiKey
//        val tokenCard = Token.create(card.createToken())
//        val source = card.createSource(tokenCard.id)
//        return customer.sources.create(source) as? Card?
//    }
//
//    override suspend fun update(customer: Customer, cardId: String, card: CardInput): Card {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun delete(customer: Customer, cardId: String): Boolean {
//        Stripe.apiKey = secretApiKey
//        val card = customer.sources.retrieve(cardId) as? Card?
//        val deletedCard = card?.delete()
//        return deletedCard?.deleted == true
//    }
//
//    override suspend fun find(customer: Customer, cardId: String): Optional<Card> {
//        Stripe.apiKey = secretApiKey
//        val card = customer.sources.retrieve(cardId) as? Card?
//        return Optional.ofNullable(card)
//    }
//
//    override suspend fun retrieve(first: Long, customer: Customer): Iterable<Card> {
//        Stripe.apiKey = secretApiKey
//        val params = PaymentSourceCollectionListParams.builder()
//            .setLimit(first)
//            .setObject("card")
//            .build()
//
//        val cards = customer.sources.list(params)
//        return cards.data.filterIsInstance<Card>()
//    }

}