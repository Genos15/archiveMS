package com.thintwice.archive.mbompay.controller

import com.thintwice.archive.mbompay.domain.input.AdminInput
import com.thintwice.archive.mbompay.domain.model.Admin
import com.thintwice.archive.mbompay.repository.AdminRepository
import org.springframework.graphql.data.method.annotation.*
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class AdminController(private val service: AdminRepository) {

    @MutationMapping(name = "admin")
    suspend fun admin(@Argument input: AdminInput, @ContextValue token: UUID): Optional<Admin> {
        return service.admin(input = input, token = token)
    }

    @QueryMapping(name = "admin")
    suspend fun admin(@Argument id: UUID, @ContextValue token: UUID): Optional<Admin> {
        return service.admin(id = id, token = token)
    }

    @QueryMapping(name = "admins")
    suspend fun admins(
        @Argument first: Int,
        @Argument after: UUID? = null,
        @ContextValue token: UUID,
    ): Iterable<Admin> {
        return service.admins(first = first, after = after, token = token)
    }

}