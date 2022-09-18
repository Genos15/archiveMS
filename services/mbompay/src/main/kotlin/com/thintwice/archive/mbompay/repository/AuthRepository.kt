package com.thintwice.archive.mbompay.repository


import com.thintwice.archive.mbompay.domain.input.LocalizationInput
import com.thintwice.archive.mbompay.domain.model.SessionToken
import java.util.*

interface AuthRepository {
    suspend fun login(imei: String, where: LocalizationInput? = null): Optional<SessionToken>
    suspend fun refresh(token: UUID, where: LocalizationInput? = null): Optional<SessionToken>
}