package com.thintwice.archive.mbompay.repository


import com.thintwice.archive.mbompay.domain.input.CardInput
import com.thintwice.archive.mbompay.domain.model.Card
import java.util.*

interface CardRepository {
    suspend fun card(input: CardInput, token: String): Optional<Card>
    suspend fun card(token: String): Optional<Card>
    suspend fun deleteCard(token: String): Boolean
    suspend fun cards(first: Long, token: String): Iterable<Card>
}