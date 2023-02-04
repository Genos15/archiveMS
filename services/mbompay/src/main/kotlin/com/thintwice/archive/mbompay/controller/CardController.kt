package com.thintwice.archive.mbompay.controller

import com.thintwice.archive.mbompay.domain.input.CardInput
import com.thintwice.archive.mbompay.domain.model.Card
import com.thintwice.archive.mbompay.domain.model.Provider
import com.thintwice.archive.mbompay.repository.CardRepository
import org.springframework.graphql.data.method.annotation.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class CardController(private val service: CardRepository) {
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @MutationMapping(name = "card")
    suspend fun card(@Argument input: CardInput, authentication: Authentication?): Optional<Card> {
        if (authentication?.name == null) {
            throw RuntimeException("invalid token")
        }
        return service.card(input = input, token = authentication.name)
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @QueryMapping(name = "card")
    suspend fun card(@Argument id: UUID, authentication: Authentication?): Optional<Card> {
        if (authentication?.name == null) {
            throw RuntimeException("invalid token")
        }
        return service.card(token = authentication.name)
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @MutationMapping(name = "deleteCard")
    suspend fun deleteCard(@Argument id: UUID, authentication: Authentication?): Boolean {
        if (authentication?.name == null) {
            throw RuntimeException("invalid token")
        }
        return service.deleteCard(token = authentication.name)
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @QueryMapping(name = "cards")
    suspend fun cards(
        @Argument first: Long,
        authentication: Authentication?,
    ): Iterable<Card> {
        if (authentication?.name == null) {
            throw RuntimeException("invalid token")
        }
        return service.cards(first = first, token = authentication.name)
    }

    @BatchMapping(field = "provider")
    suspend fun providers(cards: List<Card>): Map<Card, Provider> {
        return emptyMap()
    }
}