package com.thintwice.archive.mbompay.controller

import com.thintwice.archive.mbompay.domain.input.BankCardInput
import com.thintwice.archive.mbompay.domain.model.Card
import com.thintwice.archive.mbompay.domain.model.Provider
import com.thintwice.archive.mbompay.repository.CardRepository
import org.springframework.graphql.data.method.annotation.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class CardController(private val service: CardRepository) {
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @MutationMapping(name = "card")
    suspend fun card(@Argument input: BankCardInput, @ContextValue token: UUID): Optional<Card> {
        return service.card(input = input, token = token)
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @QueryMapping(name = "card")
    suspend fun card(@Argument id: UUID, @ContextValue token: UUID): Optional<Card> {
        return service.card(id = id, token = token)
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @MutationMapping(name = "deleteCard")
    suspend fun deleteCard(@Argument id: UUID, @ContextValue token: UUID): Boolean {
        return service.deleteCard(id = id, token = token)
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'OPERATOR')")
    @QueryMapping(name = "cards")
    suspend fun cards(
        @Argument first: Int,
        @Argument after: UUID? = null,
        @ContextValue token: UUID,
    ): Iterable<Card> {
        return service.cards(first = first, after = after, token = token)
    }

    @BatchMapping(field = "provider")
    suspend fun providers(cards: List<Card>): Map<Card, Provider> {
        return service.providers(cards = cards, token = null)
    }
}