package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.configuration.database.DbClient
import com.thintwice.archive.mbompay.configuration.security.JWTService
import com.thintwice.archive.mbompay.domain.common.parameterOrNull
import com.thintwice.archive.mbompay.domain.mapper.JwtTokenMapper
import com.thintwice.archive.mbompay.domain.model.JwtToken
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Instant


@Service
class CustomerReactiveUserDetailsService(
    private val dbClient: DbClient,
    private val qr: RB,
    private val mapper: JwtTokenMapper,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JWTService
) : ReactiveUserDetailsService {

    override fun findByUsername(username: String?): Mono<UserDetails> {
        val query = qr.l("mutation.jwt.token.authenticate")
        println("CustomerReactiveUserDetailsService -> findByUsername = $username")
        return dbClient.exec(query = query)
            .bind("token", parameterOrNull(username))
            .map(mapper::factory)
            .first()
            .handle<JwtToken> { session, sink ->
                if (session.isPresent) {
                    return@handle sink.next(session.get())
                } else {
                    return@handle sink.error(UsernameNotFoundException("invalid credentials"))
                }
            }.map {
                val decodeAccessToken = jwtService.decodeAccessToken(it.accessToken)
                val expiredAt = decodeAccessToken.expiresAt.toInstant()

                return@map User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(decodeAccessToken.keyId))
//                    .password(passwordEncoder.encode("0000"))
                    .accountExpired(expiredAt.isBefore(Instant.now()))
                    .accountLocked(expiredAt.isBefore(Instant.now()))
                    .credentialsExpired(expiredAt.isBefore(Instant.now()))
                    .disabled(false)
                    .roles(it.userRole)
                    .build()
            }.log()
            .onErrorResume {
                println("Error findByUsername ${it.message}")
                Mono.error(it)
            }
    }
}