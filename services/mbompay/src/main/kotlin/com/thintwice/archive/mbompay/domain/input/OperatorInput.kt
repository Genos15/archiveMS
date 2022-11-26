package com.thintwice.archive.mbompay.domain.input

import java.util.*

data class OperatorInput(
    override val id: UUID? = null,
    val name: String? = null,
    override val email: String? = null,
    override val phone: String? = null,
    override val passwordHash: String? = null,
    val countryId: UUID? = null,
) : UserInput(id = id, email = email, phone = phone, passwordHash = passwordHash)