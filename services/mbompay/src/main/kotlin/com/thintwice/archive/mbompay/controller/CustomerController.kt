package com.thintwice.archive.mbompay.controller

import com.thintwice.archive.mbompay.domain.input.CustomerInput
import com.thintwice.archive.mbompay.domain.model.Customer
import com.thintwice.archive.mbompay.domain.model.JwtToken
import com.thintwice.archive.mbompay.repository.CustomerRepository
import org.springframework.graphql.data.method.annotation.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class CustomerController(private val service: CustomerRepository) {
    @PreAuthorize("hasAnyRole('FIREBASE')")
    @MutationMapping(name = "customer")
    suspend fun customer(@Argument input: CustomerInput): Optional<Customer> {
        return service.customer(input = input)
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @MutationMapping(name = "customerInfo")
    suspend fun customerInfo(@Argument input: CustomerInput): Optional<Customer> {
        return service.customer(input = input)
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @QueryMapping(name = "customer")
    suspend fun customer(authentication: Authentication?): Optional<Customer> {
        if (authentication?.name == null) {
            throw RuntimeException("invalid token")
        }

        return service.customer(accessToken = authentication.name)
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @QueryMapping(name = "customerInfo")
    suspend fun customerInfo(@Argument id: UUID): Optional<Customer> {
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

    @PreAuthorize("hasAnyRole('FIREBASE')")
    @BatchMapping(field = "jwtToken")
    suspend fun jwtToken(customers: List<Customer>): Map<Customer, JwtToken> {
        return service.jwtToken(customers = customers)
    }

}