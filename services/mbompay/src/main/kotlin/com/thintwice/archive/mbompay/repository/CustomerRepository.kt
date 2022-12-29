package com.thintwice.archive.mbompay.repository


import com.thintwice.archive.mbompay.domain.input.CustomerInput
import com.thintwice.archive.mbompay.domain.model.Customer
import java.util.*

interface CustomerRepository {
    suspend fun customer(input: CustomerInput): Optional<Customer>
    suspend fun customer(id: UUID): Optional<Customer>
    suspend fun customers(first: Int, after: UUID? = null): Iterable<Customer>
}