package com.thintwice.archive.auth.services

import com.thintwice.archive.auth.domain.input.TokenInput
import com.thintwice.archive.auth.domain.model.Token
import com.thintwice.archive.auth.repository.TokenRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CategoryService(private val it: TokenRepository) {
    suspend fun login(input: TokenInput): Optional<Token> = it.login(input = input)
    suspend fun refresh(input: UUID): Optional<Token> = it.refresh(input = input)
}