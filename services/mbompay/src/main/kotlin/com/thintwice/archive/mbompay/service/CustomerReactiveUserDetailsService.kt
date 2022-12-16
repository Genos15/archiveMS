package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.configuration.database.DbClient
import com.thintwice.archive.mbompay.configuration.security.JWTService
import com.thintwice.archive.mbompay.domain.common.parameterOrNull
import com.thintwice.archive.mbompay.domain.mapper.JwtTokenMapper
import com.thintwice.archive.mbompay.domain.model.AuthUser
import com.thintwice.archive.mbompay.domain.model.JwtToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
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
    private val jwtService: JWTService,
) : ReactiveUserDetailsService {

    override fun findByUsername(username: String?): Mono<UserDetails> {
        val query = qr.l("mutation.jwt.token.authenticate")
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
                val expiredAt: Instant = decodeAccessToken.expiresAt.toInstant()
                val useRole = generateUserRole(it.userRole)
                AuthUser(
                    _username = username!!,
                    _password = passwordEncoder.encode(username),
                    roles = AuthorityUtils.createAuthorityList(useRole),
                    _isAccountNonExpired = expiredAt.isBefore(Instant.now()),
                    _isAccountNonLocked = expiredAt.isBefore(Instant.now()),
                    _isCredentialsNonExpired = expiredAt.isBefore(Instant.now()),
                    _isEnabled = true
                )
            }
    }

    private fun generateUserRole(abstractRole: String?): String {
        return if (abstractRole.isNullOrEmpty()) return ""
        else "ROLE_${abstractRole.uppercase().trim { role -> role <= ' ' }}"
    }
}