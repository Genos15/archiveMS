package com.thintwice.archive.mbompay.repository


import com.thintwice.archive.mbompay.domain.input.ProviderInput
import com.thintwice.archive.mbompay.domain.model.Provider
import java.util.*

interface ProviderRepository {
    suspend fun provider(input: ProviderInput, token: UUID): Optional<Provider>
    suspend fun provider(id: UUID, token: UUID): Optional<Provider>
    suspend fun deleteProvider(id: UUID, token: UUID): Boolean
    suspend fun providers(first: Int, after: UUID? = null, token: UUID): Iterable<Provider>
}