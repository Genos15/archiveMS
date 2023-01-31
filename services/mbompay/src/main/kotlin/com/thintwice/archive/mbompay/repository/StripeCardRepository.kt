package com.thintwice.archive.mbompay.repository

import com.stripe.model.Card
import com.stripe.model.Customer
import com.thintwice.archive.mbompay.domain.input.CardInput
import java.util.*

interface StripeCardRepository {
    suspend fun create(customer: Customer, card: CardInput): Card?
    suspend fun update(customer: Customer, cardId: String, card: CardInput): Card
    suspend fun delete(customer: Customer, cardId: String): Boolean
    suspend fun find(customer: Customer, cardId: String): Optional<Card>
    suspend fun retrieve(first: Long, customer: Customer): Iterable<Card>
}