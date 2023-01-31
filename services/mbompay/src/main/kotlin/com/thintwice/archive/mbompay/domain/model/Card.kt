package com.thintwice.archive.mbompay.domain.model

import com.stripe.model.Card

data class Card(
    val name: String,
    val last4Digits: String,
    val expiredMonth: Long,
    val expiredYear: Long,
    val country: String?,
    val brand: String?,
    val funding: String?,
) {
    constructor(stripeCard: Card) : this(
        name = stripeCard.name,
        country = stripeCard.country,
        brand = stripeCard.brand,
        expiredYear = stripeCard.expYear,
        expiredMonth = stripeCard.expMonth,
        last4Digits = stripeCard.last4,
        funding = stripeCard.funding,
    )
}

