package com.thintwice.archive.mbompay.repository

import com.stripe.model.Charge
import com.stripe.model.PaymentIntent

interface StripeChargeRepository {
    suspend fun send(amount: Long, token: String): Charge?
    suspend fun sendPayment(amount: Long, token: String): String?
    suspend fun refund(): Charge?
    suspend fun retrieve(first: Long): Iterable<Charge>
}