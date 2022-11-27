package com.thintwice.archive.mbompay.service

import com.stripe.Stripe
import com.stripe.model.Card
import com.stripe.model.Customer
import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.domain.input.BankCardInput
import com.thintwice.archive.mbompay.repository.StripeBankCardRepository
import org.springframework.stereotype.Service
import java.util.*


@Service
class StripeBankCardRepositoryImpl(sr: RB) : StripeBankCardRepository {

    private val secretApiKey = sr.l("stripe.secret.api.key")

    private fun getCustomer(customerId: String): Customer {
        val retrieveParams: MutableMap<String, Any> = HashMap()
        val expandList: MutableList<String> = ArrayList()
        expandList.add("sources")
        retrieveParams["expand"] = expandList
        return Customer.retrieve(customerId, retrieveParams, null)
    }

    override fun create(input: BankCardInput, customerId: String): BankCardInput {
        Stripe.apiKey = secretApiKey
        val card = getCustomer(customerId = customerId)
            .sources
            .create(input.toMap()) as Card
        input.stripeCardId = card.id
        return input
    }

    override fun update(input: BankCardInput, customerId: String): BankCardInput {
        Stripe.apiKey = secretApiKey
        TODO("Not yet implemented")
    }

    override fun delete(input: BankCardInput, customerId: String): Boolean {
        Stripe.apiKey = secretApiKey
        val card = getCustomer(customerId = customerId)
            .sources
            .retrieve(input.stripeCardId) as Card
        val deletedCard = card.delete() as Card
        return deletedCard.deleted
    }

    override fun find(input: BankCardInput, customerId: String): Optional<BankCardInput> {
        Stripe.apiKey = secretApiKey
        val card = getCustomer(customerId = customerId)
            .sources
            .retrieve(input.stripeCardId) as Card
        return Optional.of(
            BankCardInput(
                id = input.id,
                name = card.name,
                bcNumber = input.bcNumber,
                bcExpiredMonth = input.bcExpiredMonth,
                bcExpiredYear = input.bcExpiredYear,
                bcCvc = input.bcCvc,
                stripeCardId = card.id
            )
        )
    }

    override fun retrieve(first: Int, customerId: String): Iterable<BankCardInput> {
        Stripe.apiKey = secretApiKey
        TODO("Not yet implemented")
    }

    override fun search(options: Map<String, Any>, customerId: String): Iterable<BankCardInput> {
        Stripe.apiKey = secretApiKey
        TODO("Not yet implemented")
    }

}