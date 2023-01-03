package com.thintwice.archive.mbompay.middleware.implementation

import com.google.firebase.auth.FirebaseAuth
import com.thintwice.archive.mbompay.domain.exception.InvalidTokenException
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import reactor.core.publisher.Mono


class FirebaseAuthHandler(
    private val request: WebGraphQlRequest,
    private val chain: WebGraphQlInterceptor.Chain,
) {

    operator fun invoke(): Mono<WebGraphQlResponse> {
        val authHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        val uidHeader = request.headers.getFirst(kUID_FIREBASE_HEADER)
        val firebaseDecodedToken = getFirebaseDecodedToken(authHeader)
        return if (firebaseDecodedToken != null && firebaseDecodedToken.isEmailVerified && uidHeader == firebaseDecodedToken.uid) {
            val authentication = UsernamePasswordAuthenticationToken(
                firebaseDecodedToken.email,
                firebaseDecodedToken.uid,
                AuthorityUtils.createAuthorityList(kFIREBASE_DEFAULT_USER_ROLE),
            )

            chain.next(request).contextWrite(
                ReactiveSecurityContextHolder.withAuthentication(authentication)
            )
        } else {
            chain.next(request).map { response ->
                response.transform {
                    it.errors(listOf(InvalidTokenException(state = InvalidTokenException.State.NO_ACCESS)))
                }
            }.contextWrite(ReactiveSecurityContextHolder.clearContext())
        }
    }

    private fun getFirebaseDecodedToken(authHeader: String?) = try {
        if (hasAuthHeader(authHeader)) {
            FirebaseAuth.getInstance().verifyIdToken(authHeader)
        } else {
            throw Exception("something went wrong")
        }
    } catch (e: Exception) {
        null
    }

    private fun hasAuthHeader(authHeader: String?): Boolean {
        return authHeader?.trim { it <= ' ' }?.isNotEmpty() ?: false
    }


    companion object {
        const val kFIREBASE_DEFAULT_USER_ROLE = "ROLE_FIREBASE"
        const val kUID_FIREBASE_HEADER = "app-firebase-uid"
    }
}