package com.thintwice.archive.mbompay.configuration.security


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.thintwice.archive.mbompay.configuration.bundle.RB
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JWTService(qr: RB) {

    val secret = qr.l("jesend.jwt.secret")

    val refresh = qr.l("jesend.jwt.refresh")

    fun accessToken(username: String, expirationInMillis: Long, roles: Array<String>): String {
        return generate(username, expirationInMillis, roles, secret)
    }

    fun decodeAccessToken(accessToken: String): DecodedJWT {
        return decode(secret, accessToken)
    }

    fun refreshToken(username: String, expirationInMillis: Long, roles: Array<String>): String {
        return generate(username, expirationInMillis, roles, refresh)
    }

    fun decodeRefreshToken(refreshToken: String): DecodedJWT {
        return decode(refresh, refreshToken)
    }

    fun getRoles(decodedJWT: DecodedJWT) = decodedJWT.getClaim("role").asList(String::class.java)
        .map { SimpleGrantedAuthority(it) }

    private fun generate(username: String, expirationInMillis: Long, roles: Array<String>, signature: String): String {
        val secretKey = signature.toByteArray()
        return JWT.create()
            .withSubject(username)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationInMillis))
            .withArrayClaim("role", roles)
            .sign(Algorithm.HMAC512(secretKey))
    }

    private fun decode(signature: String, token: String): DecodedJWT {
        val secretKey = signature.toByteArray()
        return JWT.require(Algorithm.HMAC512(secretKey))
            .build()
            .verify(token.replace("bearer ", "", ignoreCase = true))
    }
}