package com.thintwice.archive.mbompay.controller

import com.thintwice.archive.mbompay.domain.input.UserInput
import com.thintwice.archive.mbompay.domain.model.JwtToken
import com.thintwice.archive.mbompay.repository.AuthRepository
import com.thintwice.archive.mbompay.repository.OneTimePasswordRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class AuthController(
    private val service: AuthRepository,
    private val otpService: OneTimePasswordRepository,
) {

    @MutationMapping(name = "connect")
    suspend fun connect(@Argument input: UserInput, @Argument code: String): JwtToken =
        otpService.verify(input = input, code = code)

    @MutationMapping(name = "otp")
    suspend fun otp(@Argument input: String): Long {
        val oneTimePassword = otpService.generate(ownerContact = input, locale = Locale.FRENCH)
        return oneTimePassword.expiredAt
    }

    @MutationMapping(name = "refresh")
    suspend fun refresh(auth: Authentication): JwtToken =
        otpService.refresh(refreshToken = "${auth.credentials}")

    @PreAuthorize("hasAnyRole('ADMIN')")
    @MutationMapping(name = "deleteUser")
    suspend fun deleteUser(
        @Argument id: UUID,
        @ContextValue token: UUID,
    ): Boolean = service.deleteUser(token = token, id = id)
}