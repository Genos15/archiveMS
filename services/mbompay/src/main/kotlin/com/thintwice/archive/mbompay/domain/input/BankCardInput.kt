package com.thintwice.archive.mbompay.domain.input

import com.thintwice.archive.mbompay.domain.common.JsonEquivalent
import com.thintwice.archive.mbompay.domain.common.MapEquivalent
import java.util.*

data class BankCardInput(
    val id: UUID? = null,
    val name: String? = null,
    val bcNumber: String? = null,
    val bcExpiredMonth: Int? = null,
    val bcExpiredYear: Int? = null,
    val bcCvc: String? = null,
    var stripeCardId: String? = null,
) : JsonEquivalent, MapEquivalent {
    override fun toMap(): Map<String, Any?> {
        return mapOf(
            "source" to mapOf(
                "object" to "card",
                "number" to bcNumber,
                "exp_month" to bcExpiredMonth,
                "exp_year" to bcExpiredYear,
                "cvc" to bcCvc,
                "name" to name,
            ),
            "metadata" to mapOf(),
        )
    }
}

