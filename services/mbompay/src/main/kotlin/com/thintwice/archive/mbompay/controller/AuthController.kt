package com.thintwice.archive.mbompay.controller

import com.thintwice.archive.mbompay.configuration.security.JWTService
import com.thintwice.archive.mbompay.domain.input.UserInput
import com.thintwice.archive.mbompay.domain.model.JwtToken
import com.thintwice.archive.mbompay.repository.AuthRepository
import com.thintwice.archive.mbompay.repository.OneTimePasswordRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class AuthController(
    private val service: AuthRepository,
    private val otpService: OneTimePasswordRepository,
    private val authenticationManager: ReactiveAuthenticationManager,
    private val jwtService: JWTService,
) {

//    @MutationMapping(name = "login")
//    suspend fun login(
//        @ContextValue(required = false) where: LocalizationInput,
//        @ContextValue imei: String,
//    ): Optional<SessionToken> = service.login(imei = imei, where = where)
//
//    @MutationMapping(name = "refresh")
//    suspend fun refresh(
//        @ContextValue(required = false) where: LocalizationInput,
//        @ContextValue token: UUID,
//    ): Optional<SessionToken> = service.refresh(token = token, where = where)

    @MutationMapping(name = "connect")
    suspend fun connect(@Argument input: UserInput, @Argument code: String): JwtToken {
//        val user = UsernamePasswordAuthenticationToken(
//            input.phone ?: input.email,
//            input.passwordHash ?: code,
//        )
//        println("Authentication process")
//        val authentication = authenticationManager.authenticate(user)
//            .awaitFirstOrNull()

//        println(
//            """
//            Authentication => $authentication
//            ${authentication?.isAuthenticated}
//            ${authentication?.credentials}
//            ${authentication?.name}
//            ${authentication?.authorities}
//        """.trimIndent()
//        )

//        return service.connect(input = input, code = code)
        return otpService.verify(input = input, code = code)
    }

    @MutationMapping(name = "otp")
    suspend fun otp(@Argument input: String): Long {
        val oneTimePassword = otpService.generate(ownerContact = input, locale = Locale.FRENCH)
        return oneTimePassword.expiredAt
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @MutationMapping(name = "refresh")
    suspend fun refresh(/*@ContextValue token: String*/authentication: Authentication): JwtToken {
        return otpService.refresh(refreshToken = "")
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @MutationMapping(name = "deleteUser")
    suspend fun deleteUser(
        @Argument id: UUID,
        @ContextValue token: UUID,
    ): Boolean = service.deleteUser(token = token, id = id)
}