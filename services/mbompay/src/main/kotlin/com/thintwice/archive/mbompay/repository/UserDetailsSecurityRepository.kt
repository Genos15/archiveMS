package com.thintwice.archive.mbompay.repository

import com.thintwice.archive.mbompay.domain.input.UserInput
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

interface UserDetailsSecurityRepository {
    fun user(input: UserInput, code: String? = null): Optional<UserDetails>
}