package com.thintwice.archive.mbompay.domain.model

import java.time.OffsetDateTime
import java.util.*

data class Customer(
    val id: UUID,
    val displayName: String? = null,
    val email: String? = null,
    val emailVerified: Boolean? = false,
    val isAnonymous: Boolean? = true,
    val phoneNumber: String? = null,
    val phoneNumberVerified: Boolean? = false,
    val photoURL: String? = null,
    val providerId: String? = null,
    val providerUid: String? = null,
    val firebaseUid: String? = null,
    val jwtToken: JwtToken? = null,
    val address: String? = null,
    val birthday: OffsetDateTime? = null,
)

