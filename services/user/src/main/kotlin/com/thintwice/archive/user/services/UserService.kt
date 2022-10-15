package com.thintwice.archive.user.services

import com.thintwice.archive.user.domain.input.UserInput
import com.thintwice.archive.user.domain.model.User
import com.thintwice.archive.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val it: UserRepository) {
    suspend fun edit(input: UserInput, token: UUID): Optional<User> = it.edit(input = input, token = token)
    suspend fun delete(id: UUID, token: UUID): Boolean = it.delete(id = id, token = token)
    suspend fun user(id: UUID? = null, token: UUID): Optional<User> = it.user(token = token, id = id)
}