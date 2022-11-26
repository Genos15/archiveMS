package com.thintwice.archive.mbompay.controller

import com.thintwice.archive.mbompay.domain.input.OperatorInput
import com.thintwice.archive.mbompay.domain.model.Operator
import com.thintwice.archive.mbompay.repository.OperatorRepository
import org.springframework.graphql.data.method.annotation.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class OperatorController(private val service: OperatorRepository) {

    @PreAuthorize("hasAnyRole('ADMIN')")
    @MutationMapping(name = "operator")
    suspend fun operator(@Argument input: OperatorInput, @ContextValue token: UUID): Optional<Operator> {
        return service.operator(input = input, token = token)
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @QueryMapping(name = "operator")
    suspend fun operator(@Argument id: UUID, @ContextValue token: UUID): Optional<Operator> {
        return service.operator(id = id, token = token)
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @QueryMapping(name = "operators")
    suspend fun operators(
        @Argument first: Int,
        @Argument after: UUID? = null,
        @ContextValue token: UUID,
    ): Iterable<Operator> {
        return service.operators(first = first, after = after, token = token)
    }

}