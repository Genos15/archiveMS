package com.thintwice.archive.mbompay.domain.model

import java.time.OffsetDateTime
import java.util.*

data class Card(
    val id: UUID,
    val number: String,
    val expiredAt: OffsetDateTime? = null,
    val provider: Provider? = null,
)