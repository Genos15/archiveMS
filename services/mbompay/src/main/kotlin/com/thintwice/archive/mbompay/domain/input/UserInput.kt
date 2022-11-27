package com.thintwice.archive.mbompay.domain.input

import com.thintwice.archive.mbompay.domain.common.JsonEquivalent
import java.util.*

open class UserInput(
    open val id: UUID? = null,
    open val email: String? = null,
    open val phone: String? = null,
    open val passwordHash: String? = null,
) : JsonEquivalent