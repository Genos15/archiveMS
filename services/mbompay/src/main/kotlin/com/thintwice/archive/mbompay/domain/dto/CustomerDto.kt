package com.thintwice.archive.mbompay.domain.dto

import com.thintwice.archive.mbompay.domain.model.Customer
import java.util.*

data class CustomerDto(
    val id: UUID,
    val name: String,
    val addressLine: String? = null,
    val postalCode: String? = null,
    val city: String? = null,
    val email: String,
    val phone: String,
    val state: String? = null,
    val isEmailVerified: Boolean? = null,
    val isPhoneVerified: Boolean? = null,
    val stripeCustomerId: String,
) {
    val customer: Customer
        get() = Customer(
            id = id,
            name = name,
            postalCode = postalCode,
            city = city,
            email = email,
            phone = phone,
            addressLine = addressLine,
            state = state,
            isEmailVerified = isEmailVerified,
            isPhoneVerified = isPhoneVerified
        )
}