package com.thintwice.archive.mbompay.domain.input

import com.stripe.model.Card
import com.thintwice.archive.mbompay.domain.common.JsonEquivalent

data class JCardInput(
    val name: String,
    val last4Digits: String,
    val expiredMonth: Long,
    val expiredYear: Long,
    val country: String?,
    val brand: String?,
    val providerId: String?,
    val customerUid: String?
) : JsonEquivalent {
    constructor(stripeCard: Card) : this(
        name = stripeCard.name ?: "NAME",
        country = stripeCard.country,
        brand = stripeCard.brand,
        expiredYear = stripeCard.expYear,
        expiredMonth = stripeCard.expMonth,
        last4Digits = stripeCard.last4,
        providerId = stripeCard.id,
        customerUid = stripeCard.customer
    )
}

