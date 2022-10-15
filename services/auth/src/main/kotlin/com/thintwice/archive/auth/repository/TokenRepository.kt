package com.thintwice.archive.auth.repository

import com.thintwice.archive.auth.domain.input.TokenInput
import com.thintwice.archive.auth.domain.model.Token
import java.util.*

interface TokenRepository {
    suspend fun login(input: TokenInput): Optional<Token>
    suspend fun refresh(input: UUID): Optional<Token>
}