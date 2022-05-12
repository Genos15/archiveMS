package com.thintwice.archive.user.repository

import com.thintwice.archive.user.domain.input.UserInput
import com.thintwice.archive.user.domain.model.User
import java.util.*

interface UserRepository {
    suspend fun edit(input: UserInput, token: UUID): Optional<User>
    suspend fun delete(id: UUID, token: UUID): Boolean
    suspend fun user(id: UUID? = null, token: UUID): Optional<User>
}