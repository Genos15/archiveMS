package com.thintwice.archive.mbompay.domain.input

import com.thintwice.archive.mbompay.domain.common.MapEquivalent
import java.util.*

data class CustomerInput(
    override val id: UUID? = null,
    val name: String? = null,
    val countryId: UUID? = null,
    val city: String? = null,
    val addressLine: String? = null,
    val postalCode: String? = null,
    val state: String? = null,
    override val email: String? = null,
    override val phone: String? = null,
    override val passwordHash: String? = null,
    val identityDocument: UUID? = null,
    var stripeCustomerId: String? = null,
) : UserInput(id = id, email = email, phone = phone, passwordHash = passwordHash), MapEquivalent {
    override fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "email" to email,
            "phone" to phone,
            "address" to mapOf(
                "city" to city,
                "line1" to addressLine,
                "postal_code" to postalCode,
                "state" to state,
            ).filter { it.value != null },
            "metadata" to mapOf("countryId" to countryId)
                .filter { it.value != null }
        ).filter { it.value != null }
    }
}
