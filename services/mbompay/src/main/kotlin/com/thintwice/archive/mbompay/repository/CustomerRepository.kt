package com.thintwice.archive.mbompay.repository


import com.stripe.model.Customer
import com.thintwice.archive.mbompay.domain.input.CustomerInput
import com.thintwice.archive.mbompay.domain.model.JCard
import com.thintwice.archive.mbompay.domain.model.JCustomer
import com.thintwice.archive.mbompay.domain.model.JwtToken
import java.util.*

interface CustomerRepository {
    suspend fun customer(input: CustomerInput): Optional<JCustomer>

    suspend fun customer(id: UUID): Optional<JCustomer>

    suspend fun customer(accessToken: String): Optional<JCustomer>

    suspend fun customers(first: Int, after: UUID? = null): Iterable<JCustomer>

    suspend fun jwtToken(customers: List<JCustomer>): Map<JCustomer, JwtToken>

    suspend fun defaultCard(customers: List<JCustomer>): Map<JCustomer, JCard>

    suspend fun updateStripeId(uid: String, accessToken: String)

    suspend fun stripeEventUpdate(customer: Customer)
}