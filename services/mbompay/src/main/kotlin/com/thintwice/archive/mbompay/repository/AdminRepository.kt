package com.thintwice.archive.mbompay.repository


import com.thintwice.archive.mbompay.domain.input.AdminInput
import com.thintwice.archive.mbompay.domain.model.Admin
import java.util.*

interface AdminRepository {
    suspend fun admin(input: AdminInput, token: UUID): Optional<Admin>
    suspend fun admin(id: UUID, token: UUID): Optional<Admin>
    suspend fun admins(first: Int, after: UUID? = null, token: UUID): Iterable<Admin>
}