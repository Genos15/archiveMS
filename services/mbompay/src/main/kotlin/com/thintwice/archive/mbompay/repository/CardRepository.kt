package com.thintwice.archive.mbompay.repository


import com.thintwice.archive.mbompay.domain.input.BankCardInput
import com.thintwice.archive.mbompay.domain.model.Card
import com.thintwice.archive.mbompay.domain.model.Provider
import java.util.*

interface CardRepository {
    suspend fun card(input: BankCardInput, token: UUID): Optional<Card>
    suspend fun card(id: UUID, token: UUID): Optional<Card>
    suspend fun deleteCard(id: UUID, token: UUID): Boolean
    suspend fun cards(first: Int, after: UUID? = null, token: UUID): Iterable<Card>
    suspend fun providers(cards: List<Card>, token: UUID? = null): Map<Card, Provider>
}