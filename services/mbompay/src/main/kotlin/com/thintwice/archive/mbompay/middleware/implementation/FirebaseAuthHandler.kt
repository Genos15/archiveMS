package com.thintwice.archive.mbompay.middleware.implementation

import com.google.firebase.auth.FirebaseAuth
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import reactor.core.publisher.Mono
import kotlin.streams.toList


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

            println("-- Authentication = $authentication")

            chain.next(request).contextWrite(
                ReactiveSecurityContextHolder.withAuthentication(authentication)
            ).onErrorResume { error ->
                println("-- FirebaseAuthHandler error")
                error.printStackTrace()
                onError(request = request, chain = chain)
            }
        } else {
            chain.next(request)
                .map(this::processResponse)
                .contextWrite(ReactiveSecurityContextHolder.clearContext())
        }
    }

    private fun getFirebaseDecodedToken(authHeader: String?) = try {
        if (hasAuthHeader(authHeader)) {
            FirebaseAuth.getInstance().verifyIdToken(authHeader)
        } else {
            throw Exception("something went wrong")
        }
    } catch (e: Exception) {
        println("-- getFirebaseDecodedToken = ${e.message}")
        null
    }

    private fun hasAuthHeader(authHeader: String?): Boolean {
        return authHeader?.trim { it <= ' ' }?.isNotEmpty() ?: false
    }

    private fun onError(chain: WebGraphQlInterceptor.Chain, request: WebGraphQlRequest): Mono<WebGraphQlResponse> {
        println("-- handler error FirebaseAuthHandler --")
        return chain.next(request)
            .map(this::processResponse)
            .contextWrite(ReactiveSecurityContextHolder.clearContext())
    }

    private fun processResponse(response: WebGraphQlResponse): WebGraphQlResponse {
        return if (response.isValid) {
            response
        } else {
            val errors: List<GraphQLError> = response.errors.stream()
                .map { error ->
                    val builder = GraphqlErrorBuilder.newError()
                    println("-- chain error firebase $error")
                    builder.build()
                }
                .toList()

            response.transform { builder ->
                builder.errors(errors).build()
            }
        }
    }

    companion object {
        const val kFIREBASE_DEFAULT_USER_ROLE = "ROLE_FIREBASE"
        const val kUID_FIREBASE_HEADER = "app-firebase-uid"
    }
}