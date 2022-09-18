package com.thintwice.archive.mbompay.controller

import com.thintwice.archive.mbompay.domain.input.LocalizationInput
import com.thintwice.archive.mbompay.domain.model.SessionToken
import com.thintwice.archive.mbompay.repository.AuthRepository
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class AuthController(private val service: AuthRepository) {

    @MutationMapping(name = "login")
    suspend fun login(
        @ContextValue(required = false) where: LocalizationInput,
        @ContextValue imei: String,
    ): Optional<SessionToken> = service.login(imei = imei, where = where)

    @MutationMapping(name = "refresh")
    suspend fun refresh(
        @ContextValue(required = false) where: LocalizationInput,
        @ContextValue token: UUID,
    ): Optional<SessionToken> = service.refresh(token = token, where = where)
}