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

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @MutationMapping(name = "customer")
    suspend fun customer(@Argument input: CustomerInput, @ContextValue token: UUID): Optional<Customer> {
        return service.customer(input = input, token = token)
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @QueryMapping(name = "customer")
    suspend fun customer(@Argument id: UUID, @ContextValue token: UUID): Optional<Customer> {
        return service.customer(id = id, token = token)
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORS')")
    @QueryMapping(name = "customers")
    suspend fun customers(
        @Argument first: Int,
        @Argument after: UUID? = null,
        @ContextValue token: UUID,
    ): Iterable<Customer> {
        return service.customers(first = first, after = after, token = token)
    }

}