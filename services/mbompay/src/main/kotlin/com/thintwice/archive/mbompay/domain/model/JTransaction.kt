package com.thintwice.archive.mbompay.domain.model

import java.time.OffsetDateTime
import java.util.UUID

data class JTransaction(
    val id: UUID,
    val uid: String,
    val amount: Long,
    val cancelledAt: Long? = null,
    val cancellationReason: String? = null,
    val clientSecret: String,
    val currency: String,
    val customerId: UUID? = null,
    val status: String,
    val createdAt: OffsetDateTime,
    val editedAt: OffsetDateTime,
    val card: JCard? = null,
)