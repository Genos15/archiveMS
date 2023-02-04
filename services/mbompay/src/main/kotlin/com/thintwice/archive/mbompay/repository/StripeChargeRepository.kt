package com.thintwice.archive.mbompay.repository

import com.stripe.model.Charge

interface StripeChargeRepository {
    suspend fun send(amount: Long, token: String): Charge?
    suspend fun refund(): Charge?
    suspend fun retrieve(first: Long): Iterable<Charge>
}