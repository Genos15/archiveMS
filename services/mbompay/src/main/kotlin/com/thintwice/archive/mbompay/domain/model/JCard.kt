package com.thintwice.archive.mbompay.domain.model

import com.thintwice.archive.mbompay.domain.common.JsonEquivalent
import java.util.UUID

data class JCard(
    val id: UUID,
    val name: String,
    val last4Digits: String,
    val expiredMonth: Long,
    val expiredYear: Long,
    val country: String?,
    val brand: String?,
    val funding: String?,
    val providerId: String?,
    val currency: String
): JsonEquivalent