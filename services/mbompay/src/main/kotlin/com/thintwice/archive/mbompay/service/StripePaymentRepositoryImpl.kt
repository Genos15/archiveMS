package com.thintwice.archive.mbompay.service

import com.stripe.Stripe
import com.stripe.model.Charge
import com.stripe.model.PaymentIntent
import com.stripe.param.ChargeRetrieveParams
import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.domain.input.PaymentIntentInput
import com.thintwice.archive.mbompay.repository.PaymentRepository
import com.thintwice.archive.mbompay.repository.StripePaymentRepository
import org.springframework.stereotype.Service


@Service
class StripePaymentRepositoryImpl(
    sr: RB,
    private val service: PaymentRepository,
) : StripePaymentRepository {

    private val secretApiKey = sr.l("stripe.secret.api.key")

    override suspend fun onCreate(intent: PaymentIntent) {
        println("stripe payment created")
        val input = PaymentIntentInput(intent = intent, status = "created")
        service.update(input = input)
    }

    override suspend fun onSucceed(intent: PaymentIntent) {
        println("stripe payment succeed")
        val input = PaymentIntentInput(intent = intent, status = "succeeded")
        service.update(input = input)
    }

    override suspend fun onSucceed(charge: Charge) {
        println("stripe charge succeed")
        Stripe.apiKey = secretApiKey
        val params = ChargeRetrieveParams.builder()
            .addExpand("payment_intent")
            .addExpand("source")
            .build()
        val deepCharge = Charge.retrieve(charge.id, params, null)
        val input = PaymentIntentInput(intent = deepCharge, status = "succeeded")
        service.update(input = input)
    }

    override suspend fun onRequiredAction(intent: PaymentIntent) {
        println("stripe payment requires action")
        val input = PaymentIntentInput(intent = intent)
        service.update(input = input)
    }

    override suspend fun onProcessing(intent: PaymentIntent) {
        println("stripe payment is processing")
        val input = PaymentIntentInput(intent = intent, status = "processing")
        service.update(input = input)
    }

    override suspend fun onFailed(intent: PaymentIntent) {
        println("stripe payment failed")
        val input = PaymentIntentInput(intent = intent, status = "failed")
        service.update(input = input)
    }

    override suspend fun onFailed(charge: Charge) {
        println("stripe charge failed")
        Stripe.apiKey = secretApiKey
        val params = ChargeRetrieveParams.builder()
            .addExpand("payment_intent")
            .addExpand("source")
            .build()
        val deepCharge = Charge.retrieve(charge.id, params, null)
        val input = PaymentIntentInput(intent = deepCharge, status = "failed")
        service.update(input = input)
    }

    override suspend fun onCancelled(intent: PaymentIntent) {
        println("stripe payment cancelled")
        val input = PaymentIntentInput(intent = intent)
        service.update(input = input)
    }

}