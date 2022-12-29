package com.thintwice.archive.mbompay.repository

import com.thintwice.archive.mbompay.domain.input.UserDetailInput
import com.thintwice.archive.mbompay.domain.model.UserDetail
import java.util.*

interface UserRepository {
    suspend fun userDetail(input: UserDetailInput): Optional<UserDetail>
}