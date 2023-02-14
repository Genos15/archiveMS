package com.thintwice.archive.mbompay.service

import com.stripe.model.PaymentIntent
import com.thintwice.archive.mbompay.domain.input.PaymentIntentInput
import com.thintwice.archive.mbompay.repository.PaymentRepository
import com.thintwice.archive.mbompay.repository.StripePaymentRepository
import org.springframework.stereotype.Service


@Service
class StripePaymentRepositoryImpl(
    private val service: PaymentRepository,
) : StripePaymentRepository {

    override suspend fun onCreate(intent: PaymentIntent) {
        println("stripe payment created")
        val input = PaymentIntentInput(intent = intent)
        service.update(input = input)
    }

    override suspend fun onSucceed(intent: PaymentIntent) {
        println("stripe payment succeed")
        val input = PaymentIntentInput(intent = intent)
        service.update(input = input)
    }

    override suspend fun onRequiredAction(intent: PaymentIntent) {
        println("stripe payment requires action")
        val input = PaymentIntentInput(intent = intent)
        service.update(input = input)
    }

    override suspend fun onProcessing(intent: PaymentIntent) {
        println("stripe payment is processing")
        val input = PaymentIntentInput(intent = intent)
        service.update(input = input)
    }

    override suspend fun onFailed(intent: PaymentIntent) {
        println("stripe payment failed")
        val input = PaymentIntentInput(intent = intent)
        service.update(input = input)
    }

    override suspend fun onCancelled(intent: PaymentIntent) {
        println("stripe payment cancelled")
        val input = PaymentIntentInput(intent = intent)
        service.update(input = input)
    }

}