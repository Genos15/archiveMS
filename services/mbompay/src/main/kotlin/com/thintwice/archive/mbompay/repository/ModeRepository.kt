package com.thintwice.archive.mbompay.repository


import com.thintwice.archive.mbompay.domain.input.ModeInput
import com.thintwice.archive.mbompay.domain.model.Country
import com.thintwice.archive.mbompay.domain.model.Mode
import java.util.*

interface ModeRepository {
    suspend fun mode(input: ModeInput, token: UUID): Optional<Mode>
    suspend fun mode(id: UUID, token: UUID): Optional<Mode>
    suspend fun deleteMode(id: UUID, token: UUID): Boolean
    suspend fun modes(first: Int, after: UUID? = null, token: UUID): Iterable<Mode>
    suspend fun country(modes: List<Mode>, token: UUID? = null): Map<Mode, Country>
}