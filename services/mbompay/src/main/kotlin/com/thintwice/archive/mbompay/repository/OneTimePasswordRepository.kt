package com.thintwice.archive.mbompay.repository

import com.thintwice.archive.mbompay.domain.input.UserInput
import com.thintwice.archive.mbompay.domain.model.JwtToken
import com.thintwice.archive.mbompay.domain.model.OneTimePassword
import java.util.*

interface OneTimePasswordRepository {
    suspend fun generate(ownerContact: String, locale: Locale = Locale.ENGLISH): OneTimePassword

    suspend fun verify(input: UserInput, code: String): JwtToken

    suspend fun refresh(refreshToken: String): JwtToken
}