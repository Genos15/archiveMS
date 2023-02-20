package com.thintwice.archive.mbompay.domain.model

import com.stripe.param.CustomerCreateParams
import com.stripe.param.CustomerUpdateParams
import java.time.OffsetDateTime
import java.util.*

data class JCustomer(
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
    val defaultCard: JCard? = null,
    val address: String? = null,
    val birthday: OffsetDateTime? = null,
    val stripeUid: String? = null,
) {
    fun createSource(): CustomerCreateParams {
        val address = CustomerCreateParams.Address
            .builder()
            .setLine1(address)
            .build()
        return CustomerCreateParams.builder()
            .setEmail(email)
            .setName(displayName)
            .setPhone(phoneNumber)
            .setAddress(address)
            .addExpand("sources")
            .build()
    }

    fun updateSource(): CustomerUpdateParams {
        return CustomerUpdateParams.builder()
            .setEmail(email)
            .setName(displayName)
            .setPhone(phoneNumber)
            .addExpand("sources")
            .build()
    }
}

