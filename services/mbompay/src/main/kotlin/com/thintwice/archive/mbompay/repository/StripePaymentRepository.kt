package com.thintwice.archive.mbompay.repository

import com.stripe.model.Charge
import com.stripe.model.PaymentIntent

interface StripePaymentRepository {
    suspend fun onCreate(intent: PaymentIntent)

    suspend fun onSucceed(intent: PaymentIntent)

    suspend fun onSucceed(charge: Charge)

    suspend fun onRequiredAction(intent: PaymentIntent)

    suspend fun onProcessing(intent: PaymentIntent)

    suspend fun onFailed(intent: PaymentIntent)
    suspend fun onFailed(charge: Charge)

    suspend fun onCancelled(intent: PaymentIntent)
}