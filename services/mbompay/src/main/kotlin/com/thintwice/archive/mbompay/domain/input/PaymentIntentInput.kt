package com.thintwice.archive.mbompay.domain.input

import com.stripe.model.PaymentIntent
import com.thintwice.archive.mbompay.domain.common.JsonEquivalent

data class PaymentIntentInput(
    val uid: String,
    val amount: Long,
    val cancelledAt: Long? = null,
    val cancellationReason: String? = null,
    val clientSecret: String,
    val currency: String,
    val customerId: String,
    val paymentMethod: String? = null,
    val status: String,
) : JsonEquivalent {

    constructor(intent: PaymentIntent) : this(
        uid = intent.id,
        amount = intent.amount,
        cancelledAt = intent.canceledAt,
        cancellationReason = intent.cancellationReason,
        clientSecret = intent.clientSecret,
        currency = intent.currency,
        customerId = intent.customer,
        paymentMethod = intent.paymentMethod,
        status = intent.status
    )
}