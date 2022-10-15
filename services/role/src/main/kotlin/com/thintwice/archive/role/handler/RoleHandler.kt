package com.thintwice.archive.role.handler

import com.thintwice.archive.role.service.RoleService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class RoleHandler(private val service: RoleService) {

    suspend fun retrieve(request: ServerRequest): ServerResponse =
        ServerResponse.ok().json().bodyValueAndAwait(service.retrieve())
}