package com.thintwice.archive.mbompay.domain.input

import com.stripe.model.Charge
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

    constructor(intent: PaymentIntent, status: String? = null) : this(
        uid = intent.id,
        amount = intent.amount,
        cancelledAt = intent.canceledAt,
        cancellationReason = intent.cancellationReason,
        clientSecret = intent.clientSecret,
        currency = intent.currency,
        customerId = intent.customer,
        paymentMethod = intent.source,
        status = status ?: intent.status
    )

    constructor(intent: Charge, status: String? = null) : this(
        uid = intent.paymentIntent,
        amount = intent.amount,
        cancelledAt = intent.paymentIntentObject?.canceledAt,
        cancellationReason = intent.paymentIntentObject?.cancellationReason,
        clientSecret = intent.paymentIntentObject.clientSecret,
        currency = intent.currency,
        customerId = intent.customer,
        paymentMethod = intent.source.id,
        status = status ?: intent.status
    )
}