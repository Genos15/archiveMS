package com.thintwice.archive.mbompay.repository


import com.stripe.model.Card
import com.thintwice.archive.mbompay.domain.input.CardInput
import com.thintwice.archive.mbompay.domain.model.JCard
import java.util.*

interface CardRepository {
    suspend fun create(input: CardInput, token: String): Optional<JCard>
    suspend fun find(id: UUID, token: String): Optional<JCard>
    suspend fun delete(id: UUID, token: String): Boolean
    suspend fun stripeEventDelete(card: Card): Boolean
    suspend fun stripeEventCreate(card: Card): Boolean
    suspend fun cards(first: Long, after: UUID? = null, token: String): Iterable<JCard>
}