package com.thintwice.archive.mbompay.controller

import com.stripe.exception.StripeException
import com.thintwice.archive.mbompay.domain.model.JCard
import com.thintwice.archive.mbompay.repository.CardRepository
import org.springframework.graphql.data.method.annotation.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class CardController(private val service: CardRepository) {

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @MutationMapping(name = "ticket")
    suspend fun create(@Argument input: String, authentication: Authentication?): String? {
        val result = runCatching {
            if (authentication?.name == null) {
                throw RuntimeException("invalid token")
            }
            return service.create(input = input, token = authentication.name)
        }.recover {
            when (it) {
                is StripeException -> {
                    println("StripeException Exception $it")
                }

                else -> println("Unknown Exception $it")
            }
            null
        }

        return result.getOrNull()
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @MutationMapping(name = "defaultCard")
    suspend fun defaultCard(@Argument id: UUID, authentication: Authentication?): Optional<JCard> {
        val result = runCatching {
            if (authentication?.name == null) {
                throw RuntimeException("invalid token")
            }
            return service.defaultCard(id = id, token = authentication.name)
        }.recover {
            when (it) {
                is StripeException -> {
                    println("StripeException Exception $it")
                }

                else -> println("Unknown Exception $it")
            }
            Optional.empty<JCard>()
        }

        return result.getOrDefault(Optional.empty())
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @MutationMapping(name = "deleteCard")
    suspend fun deleteCard(@Argument id: UUID, authentication: Authentication?): Boolean {
        val result = runCatching {
            if (authentication?.name == null) {
                throw RuntimeException("invalid token")
            }
            return service.delete(token = authentication.name, id = id)
        }.recover {
            when (it) {
                is StripeException -> {
                    println("StripeException Exception $it")
                }

                else -> println("Unknown Exception $it")
            }
            false
        }

        return result.getOrNull()!!
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @QueryMapping(name = "cards")
    suspend fun cards(
        @Argument first: Long,
        @Argument after: UUID?,
        authentication: Authentication?,
    ): Iterable<JCard> {

        val result = runCatching {
            if (authentication?.name == null) {
                throw RuntimeException("invalid token")
            }
            return service.cards(first = first, token = authentication.name, after = after)
        }.recover {
            when (it) {
                is StripeException -> {
                    println("StripeException Exception $it")
                }

                else -> println("Unknown Exception $it")
            }
            emptyList<JCard>()
        }

        return result.getOrNull()!!
    }
}