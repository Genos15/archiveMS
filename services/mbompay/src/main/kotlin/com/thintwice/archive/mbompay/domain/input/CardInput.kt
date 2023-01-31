package com.thintwice.archive.mbompay.domain.input

import com.stripe.param.PaymentSourceCollectionCreateParams
import com.stripe.param.TokenCreateParams
import com.thintwice.archive.mbompay.domain.common.JsonEquivalent

data class CardInput(
    val name: String,
    val number: String,
    val expiredMonth: Int,
    val expiredYear: Int,
    val cvc: String,
) : JsonEquivalent {

    fun createToken(): TokenCreateParams {
        val card = TokenCreateParams.Card.builder()
            .setNumber(number)
            .setExpMonth("$expiredMonth")
            .setExpYear("$expiredYear")
            .setCvc(cvc)
            .setName(name)
            .build()
        return TokenCreateParams.builder()
            .setCard(card)
            .build()
    }

    fun createSource(tokenId: String): PaymentSourceCollectionCreateParams {
        return PaymentSourceCollectionCreateParams.builder()
            .setSource(tokenId)
            .build()
    }

}

