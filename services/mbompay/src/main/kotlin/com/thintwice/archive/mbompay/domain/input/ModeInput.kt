package com.thintwice.archive.mbompay.domain.input

import com.thintwice.archive.mbompay.domain.common.JsonEquivalent
import java.util.*

data class ModeInput(
    val id: UUID? = null,
    val name: String? = null,
    val countryId: UUID? = null,
    val marks: List<String>? = emptyList(),
    val logo: UUID? = null,
): JsonEquivalent