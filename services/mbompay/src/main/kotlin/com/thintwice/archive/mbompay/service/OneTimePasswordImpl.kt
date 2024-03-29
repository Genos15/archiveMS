package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.configuration.database.DbClient
import com.thintwice.archive.mbompay.configuration.security.JWTService
import com.thintwice.archive.mbompay.domain.common.jsonOf
import com.thintwice.archive.mbompay.domain.exception.InternalException
import com.thintwice.archive.mbompay.domain.input.UserInput
import com.thintwice.archive.mbompay.domain.mapper.CredentialMapper
import com.thintwice.archive.mbompay.domain.mapper.JwtTokenMapper
import com.thintwice.archive.mbompay.domain.mapper.OneTimePasswordMapper
import com.thintwice.archive.mbompay.domain.model.JwtToken
import com.thintwice.archive.mbompay.domain.model.OneTimePassword
import com.thintwice.archive.mbompay.notification.EmailService
import com.thintwice.archive.mbompay.repository.OneTimePasswordRepository
import kotlinx.coroutines.reactive.awaitFirstOrDefault
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Clock
import java.time.ZonedDateTime
import java.util.*
import kotlin.random.Random

@Service
class OneTimePasswordImpl(
    private val qr: RB,
    private val dbClient: DbClient,
    private val mapper: OneTimePasswordMapper,
    private val jwtMapper: JwtTokenMapper,
    private val notificationService: EmailService,
    private val jwtService: JWTService,
    private val credentialMapper: CredentialMapper,
    private val clock: Clock,
) : OneTimePasswordRepository {

    override suspend fun generate(ownerContact: String, locale: Locale): OneTimePassword {
        val generatedCode = createRandomOneTimePassword().toString()
        val expiredAt = ZonedDateTime
            .now(clock)
            .plusMinutes(5)
            .toInstant()
            .toEpochMilli()

        val oneTimePassword = OneTimePassword(
            id = UUID.randomUUID(),
            code = generatedCode,
            expiredAt = expiredAt,
            ownerContact = ownerContact,
            locale = locale.language
        )
        val otp =
            create(oneTimePassword = oneTimePassword).orElseThrow { InternalException(source = "impossible to build the otp") }

        notificationService.sentMailOtp(
            recipientEmail = otp.ownerContact, recipientName = otp.ownerName, locale = locale, code = otp.code
        )
        return otp
    }

    override suspend fun verify(input: UserInput, code: String): JwtToken {

        val query = qr.l("mutation.verify.one.time.password")

        val credentials = dbClient.exec(query = query)
            .bind("input", jsonOf(input))
            .bind("code", code)
            .map(credentialMapper::one)
            .first()
            .awaitFirstOrElse { Optional.empty() }
            .orElseThrow { InternalException("invalid credentials") }

        val createJwtTokenQuery = qr.l("mutation.jwt.token.create")

        val jwtToken = jwtService.generateJwtToken(
            username = credentials.getUsername(),
            roles = arrayOf(credentials.getUserRole())
        )

        return dbClient.exec(query = createJwtTokenQuery)
            .bind("input", jsonOf(jwtToken))
            .bind("username", credentials.getUsername())
            .map(jwtMapper::factory)
            .first()
            .awaitFirstOrElse { Optional.empty() }
            .orElseThrow { InternalException("impossible to build token") }
    }

    override suspend fun refresh(refreshToken: String): JwtToken {
        val query = qr.l("mutation.jwt.token.refresh")
        val timeValidity = qr.l("jesend.otp.time.validity")
        val validTime = timeValidity.toLongOrNull() ?: throw NumberFormatException()

        val decodedToken = jwtService.decodeRefreshToken(refreshToken)
        val expiredAt = ZonedDateTime
            .now()
            .plusMinutes(validTime)
            .toInstant()
            .toEpochMilli()

        val accessToken = jwtService.accessToken(
            username = decodedToken.subject,
            roles = arrayOf(),
            expirationInMillis = expiredAt
        )

        val refreshTokenTransformed = refreshToken.replace("bearer ", "", ignoreCase = true)
        val token = JwtToken(accessToken = accessToken, refreshToken = refreshTokenTransformed, expiredAt = expiredAt)

        return dbClient.exec(query = query)
            .bind("input", jsonOf(token))
            .map(jwtMapper::factory)
            .first()
            .awaitFirstOrDefault(Optional.empty())
            .orElseThrow { InternalException("impossible to build token") }
    }

    protected fun createRandomOneTimePassword(): CharSequence {
        val length = qr.l("jesend.otp.length")
        val otpSize = length.toInt()
        val random = Random.Default
        val oneTimePassword = StringBuilder()
        for (i in 0 until otpSize) {
            val randomNumber: Int = random.nextInt(10)
            oneTimePassword.append(randomNumber)
        }
        return oneTimePassword.trim { it <= ' ' }
    }

    private suspend fun create(oneTimePassword: OneTimePassword): Optional<OneTimePassword> {

        val query = qr.l("mutation.create.edit.one.time.password")

        return dbClient.exec(query = query)
            .bind("input", jsonOf(oneTimePassword))
            .map(mapper::factory)
            .first().log()
            .doOnError { println { it.message } }
            .onErrorResume {
                Mono.error(InternalException(source = it.message))
            }.awaitFirstOrElse { Optional.empty() }
    }
}