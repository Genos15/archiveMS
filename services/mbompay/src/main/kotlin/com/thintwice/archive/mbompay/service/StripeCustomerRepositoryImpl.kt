package com.thintwice.archive.mbompay.service

import com.stripe.Stripe
import com.stripe.model.Customer
import com.stripe.param.CustomerListParams
import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.domain.model.JCustomer
import com.thintwice.archive.mbompay.repository.CustomerRepository
import com.thintwice.archive.mbompay.repository.StripeCustomerRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Service
import java.util.*


@Service
class StripeCustomerRepositoryImpl(
    private val customerRepo: CustomerRepository,
    sr: RB,
) : StripeCustomerRepository {

    private val secretApiKey = sr.l("stripe.secret.api.key")

    override suspend fun create(customer: JCustomer, source: Customer): Customer {
        Stripe.apiKey = secretApiKey
        return Customer.create(customer.createSource())
    }

    override suspend fun update(customer: JCustomer, source: Customer): Customer {
        Stripe.apiKey = secretApiKey
        return source.update(customer.updateSource())
    }

    override suspend fun delete(customer: JCustomer, source: Customer): Boolean {
        Stripe.apiKey = secretApiKey
        val deletedCustomer = source.delete()
        return deletedCustomer.deleted
    }

    override suspend fun find(customer: JCustomer): Optional<Customer> {
        Stripe.apiKey = secretApiKey
        if (customer.stripeUid == null) throw IllegalStateException()
        val stripeCustomer = Customer.retrieve(customer.stripeUid)
        return Optional.ofNullable(stripeCustomer)
    }

    /**
     * this function can also be used to filter on the list of users
     * for instance to get user with email = ? options.put("email", ?)
     */
    override suspend fun retrieve(first: Long, options: Map<String, Any>): Iterable<Customer> {
        Stripe.apiKey = secretApiKey
        val params = CustomerListParams.builder()
            .setLimit(first)
            .addExpand("data.sources")
            .putAllExtraParam(options)
            .build()
        val customers = Customer.list(params)
        return customers.data
    }

    override suspend fun activeCustomer(accessToken: String): Customer = mono {
        val dbCustomer = customerRepo.customer(accessToken = accessToken)
        if (dbCustomer.isPresent.not() || dbCustomer.get().email == null) {
            throw Exception("Bad user")
        }
        val customer = dbCustomer.get()
        val customerOffset = retrieve(first = 10, options = mapOf("email" to customer.email!!))
        return@mono if (customerOffset.toList().isEmpty()) {
            create(customer = customer, source = customerOffset.first())
        } else {
            update(customer = customer, source = customerOffset.first())
        }
    }.awaitFirstOrElse { throw RuntimeException("something went wrong") }
}