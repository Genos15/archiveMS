package com.thintwice.archive.mbompay.service

import com.stripe.Stripe
import com.stripe.model.Card
import com.stripe.model.Charge
import com.stripe.model.PaymentIntent
import com.stripe.param.ChargeCreateParams
import com.stripe.param.PaymentIntentConfirmParams
import com.stripe.param.PaymentIntentCreateParams
import com.stripe.param.PaymentSourceCollectionListParams
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
//            .setApplicationFeeAmount(2_000)
            .setSource(defaultCard.id)
            .build()
        println("-- has created charge")
        return Charge.create(chargeSource)
    }

    override suspend fun sendPayment(amount: Long, token: String): String? {
        Stripe.apiKey = secretApiKey
        val customer = customerFactory.activeCustomer(accessToken = token)
        val sourceParams = PaymentSourceCollectionListParams
            .builder()
            .setLimit(1)
            .build()

        val customerCards = customer.sources.list(sourceParams)
        val defaultCard = customerCards.data.filterIsInstance<Card>().firstOrNull()

        if (customerCards == null || defaultCard == null) {
            throw RuntimeException("no payment method registered")
        }

        val params = PaymentIntentCreateParams.builder()
            .setAmount(amount)
            .setCurrency("eur")
            .setCustomer(customer.id)
            .addPaymentMethodType(kDEFAULT_PAYMENT_METHOD)
            .setPaymentMethod(defaultCard.id)
            .build()

        val paymentIntent = PaymentIntent.create(params)
//        return confirm(intent = paymentIntent.clientSecret)
        return confirm(intent = paymentIntent.id, paymentMethod = defaultCard)
    }

    fun confirm(intent: String, paymentMethod: Card): String? {
        val paymentIntent = PaymentIntent.retrieve(intent)

        val params = PaymentIntentConfirmParams.builder()
            .setPaymentMethod(paymentMethod.id)
            .build()

        val confirmation = paymentIntent.confirm(params)

        return confirmation.clientSecret
    }

    override suspend fun refund(): Charge? {
        Stripe.apiKey = secretApiKey
        TODO("Not yet implemented")
    }

    override suspend fun retrieve(first: Long): Iterable<Charge> {
        Stripe.apiKey = secretApiKey
        TODO("Not yet implemented")
    }

    override suspend fun createPaymentIntentEventHandler(intent: PaymentIntent) {
        println(
            """
            -- created payment intent
            ${intent.id}
            ${intent.amount}
            ${intent.status}
        """.trimIndent()
        )
    }



    companion object {
        const val kDEFAULT_PAYMENT_METHOD = "card"
    }

}