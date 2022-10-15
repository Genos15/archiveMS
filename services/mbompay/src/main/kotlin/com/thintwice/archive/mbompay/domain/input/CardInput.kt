package com.thintwice.archive.mbompay.domain.input

import com.thintwice.archive.mbompay.domain.common.JsonEquivalent
import java.time.OffsetDateTime
import java.util.*

data class CardInput(
    val id: UUID? = null,
    val number: String? = null,
    val expiredAt: OffsetDateTime? = null,
    val providerId: UUID? = null,
) : JsonEquivalent