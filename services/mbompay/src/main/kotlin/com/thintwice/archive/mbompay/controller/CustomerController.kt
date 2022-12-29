package com.thintwice.archive.mbompay.controller

import com.thintwice.archive.mbompay.domain.input.CustomerInput
import com.thintwice.archive.mbompay.domain.model.Customer
import com.thintwice.archive.mbompay.repository.CustomerRepository
import org.springframework.graphql.data.method.annotation.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class CustomerController(private val service: CustomerRepository) {

    @MutationMapping(name = "customer")
    suspend fun customer(@Argument input: CustomerInput): Optional<Customer> {
        return service.customer(input = input)
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @QueryMapping(name = "customer")
    suspend fun customer(@Argument id: UUID): Optional<Customer> {
        return service.customer(id = id)
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORS')")
    @QueryMapping(name = "customers")
    suspend fun customers(
        @Argument first: Int,
        @Argument after: UUID? = null,
    ): Iterable<Customer> {
        return service.customers(first = first, after = after)
    }

}