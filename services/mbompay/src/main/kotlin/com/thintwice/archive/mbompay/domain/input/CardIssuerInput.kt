package com.thintwice.archive.mbompay.domain.input

import com.thintwice.archive.mbompay.domain.common.JsonEquivalent
import java.util.UUID

data class CardIssuerInput(
    val id: UUID,
    val cvc: String,
) : JsonEquivalent

