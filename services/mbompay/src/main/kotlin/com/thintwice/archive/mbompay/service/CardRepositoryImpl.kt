package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.domain.input.CardInput
import com.thintwice.archive.mbompay.domain.model.Card
import com.thintwice.archive.mbompay.repository.CardRepository
import com.thintwice.archive.mbompay.repository.StripeCardRepository
import com.thintwice.archive.mbompay.repository.StripeCustomerRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CardRepositoryImpl(
    private val customerFactory: StripeCustomerRepository,
    private val cardFactory: StripeCardRepository,
) : CardRepository {

    override suspend fun card(input: CardInput, token: String): Optional<Card> {
        val customer = customerFactory.activeCustomer(accessToken = token)
        val stripCard = cardFactory.create(customer = customer, card = input)
        return Optional.ofNullable(stripCard?.let { Card(stripeCard = it) })
    }

    override suspend fun card(token: String): Optional<Card> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCard(token: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun cards(first: Long, token: String): Iterable<Card> {
        val customer = customerFactory.activeCustomer(accessToken = token)
        val stripCards = cardFactory.retrieve(customer = customer, first = first)
        return stripCards.map { Card(stripeCard = it) }
    }
}