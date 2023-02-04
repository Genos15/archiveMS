package com.thintwice.archive.mbompay.repository

import com.stripe.model.Customer
import com.thintwice.archive.mbompay.domain.model.JCustomer

import java.util.*

interface StripeCustomerRepository {
    suspend fun create(customer: JCustomer, source: Customer): Customer

    suspend fun update(customer: JCustomer, source: Customer): Customer

    suspend fun delete(customer: JCustomer, source: Customer): Boolean

    suspend fun find(customer: JCustomer): Optional<Customer>

    suspend fun retrieve(first: Long, options: Map<String, Any>): Iterable<Customer>

    suspend fun activeCustomer(accessToken: String): Customer
}