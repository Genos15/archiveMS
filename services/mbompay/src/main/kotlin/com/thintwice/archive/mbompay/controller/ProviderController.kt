package com.thintwice.archive.mbompay.controller

import com.thintwice.archive.mbompay.domain.input.ProviderInput
import com.thintwice.archive.mbompay.domain.model.Provider
import com.thintwice.archive.mbompay.repository.ProviderRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class ProviderController(private val service: ProviderRepository) {

    @MutationMapping(name = "provider")
    suspend fun provider(@Argument input: ProviderInput, @ContextValue token: UUID): Optional<Provider> =
        service.provider(input = input, token = token)

    @QueryMapping(name = "provider")
    suspend fun provider(@Argument id: UUID, @ContextValue token: UUID): Optional<Provider> =
        service.provider(id = id, token = token)

    @MutationMapping(name = "deleteProvider")
    suspend fun deleteProvider(@Argument id: UUID, @ContextValue token: UUID): Boolean =
        service.deleteProvider(id = id, token = token)

    @QueryMapping(name = "providers")
    suspend fun providers(
        @Argument first: Int,
        @Argument after: UUID? = null,
        @ContextValue token: UUID,
    ): Iterable<Provider> = service.providers(first = first, after = after, token = token)
}