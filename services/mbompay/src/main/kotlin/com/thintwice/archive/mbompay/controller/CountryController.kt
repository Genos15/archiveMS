package com.thintwice.archive.mbompay.controller

import com.thintwice.archive.mbompay.domain.model.Country
import com.thintwice.archive.mbompay.domain.model.Mode
import com.thintwice.archive.mbompay.repository.CountryRepository
import org.springframework.graphql.data.method.annotation.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class CountryController(private val service: CountryRepository) {
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @QueryMapping(name = "country")
    suspend fun country(@Argument id: UUID, @ContextValue token: UUID): Optional<Country> {
        return service.country(id = id, token = token)
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @QueryMapping(name = "countries")
    suspend fun countries(
        @Argument first: Int,
        @Argument after: UUID? = null,
        @ContextValue token: String,
        authentication: Authentication,
    ): Iterable<Country> {
        return service.countries(first = first, after = after)
    }

    @BatchMapping(field = "modes")
    suspend fun modes(countries: List<Country>): Map<Country, Iterable<Mode>> {
        return service.modes(countries = countries, token = null)
    }
}