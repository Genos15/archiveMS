package com.thintwice.archive.mbompay.service

import com.stripe.Stripe
import com.stripe.model.Customer
import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.domain.input.CustomerInput
import com.thintwice.archive.mbompay.repository.StripeCustomerRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class StripeCustomerRepositoryImpl(sr: RB) : StripeCustomerRepository {

    private val secretApiKey = sr.l("stripe.secret.api.key")

    override fun create(input: CustomerInput): CustomerInput {
        Stripe.apiKey = secretApiKey
//        val customer = Customer.create(input.toMap())
//        input.stripeCustomerId = customer.id
//        return input
        TODO("Not yet implemented")
    }

    override fun update(input: CustomerInput): CustomerInput {
//        if (input.stripeCustomerId == null) return input
//        Stripe.apiKey = secretApiKey
//        val customer = Customer.retrieve(input.stripeCustomerId)
//        val updatedCustomer = customer.update(input.toMap())
//        input.stripeCustomerId = updatedCustomer.id
//        return input
        TODO("Not yet implemented")
    }

    override fun delete(input: CustomerInput): Boolean {
//        Stripe.apiKey = secretApiKey
//        val customer = Customer.retrieve(input.stripeCustomerId)
//        val deletedCustomer = customer.delete()
//        return deletedCustomer.deleted
        TODO("Not yet implemented")
    }

    override fun find(input: CustomerInput): Optional<CustomerInput> {
        Stripe.apiKey = secretApiKey
//        val customer = Customer.retrieve(input.stripeCustomerId)
        val customerInput =
            CustomerInput(
//                name = customer.name,
//                countryId = null,
//                city = customer.address.city,
//                addressLine = customer.address.line1,
//                postalCode = customer.address.postalCode,
//                state = customer.address.state,
//                email = customer.email,
//                phone = customer.phone,
//                stripeCustomerId = customer.id,
            )
        return Optional.of(customerInput)
    }

    override fun retrieve(first: Int): Iterable<CustomerInput> {
        Stripe.apiKey = secretApiKey
        return emptyList()
    }

    override fun search(options: Map<String, Any>): Iterable<CustomerInput> {
        Stripe.apiKey = secretApiKey
        return emptyList()
    }

}