package com.thintwice.archive.mbompay.controller

import com.thintwice.archive.mbompay.domain.input.ModeInput
import com.thintwice.archive.mbompay.domain.model.Country
import com.thintwice.archive.mbompay.domain.model.Mode
import com.thintwice.archive.mbompay.repository.ModeRepository
import org.springframework.graphql.data.method.annotation.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class ModeController(private val service: ModeRepository) {

    @PreAuthorize("hasAnyRole('ADMIN')")
    @MutationMapping(name = "mode")
    suspend fun mode(@Argument input: ModeInput, @ContextValue token: UUID): Optional<Mode> {
        return service.mode(input = input, token = token)
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORS')")
    @QueryMapping(name = "mode")
    suspend fun mode(@Argument id: UUID, @ContextValue token: UUID): Optional<Mode> {
        return service.mode(id = id, token = token)
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @MutationMapping(name = "deleteMode")
    suspend fun deleteMode(@Argument id: UUID, @ContextValue token: UUID): Boolean {
        return service.deleteMode(id = id, token = token)
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORS')")
    @QueryMapping(name = "modes")
    suspend fun modes(
        @Argument first: Int,
        @Argument after: UUID? = null,
        @ContextValue token: UUID,
    ): Iterable<Mode> {
        return service.modes(first = first, after = after, token = token)
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATORS')")
    @BatchMapping(field = "country")
    suspend fun country(modes: List<Mode>): Map<Mode, Country> {
        return service.country(modes = modes, token = null)
    }
}