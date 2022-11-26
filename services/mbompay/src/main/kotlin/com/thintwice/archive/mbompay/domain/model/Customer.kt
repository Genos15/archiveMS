package com.thintwice.archive.mbompay.domain.model

import java.util.*

data class Customer(
    val id: UUID,
    val name: String,
    val country: Country? = null,
    val city: String? = null,
    val addressLine: String? = null,
    val postalCode: String? = null,
    val state: String? = null,
    val email: String,
    val phone: String,
    val isPhoneVerified: Boolean? = false,
    val isEmailVerified: Boolean? = null,
) : User(id = id, name = name, email = email, phone = phone)

