package com.thintwice.archive.mbompay.domain.input

import com.thintwice.archive.mbompay.domain.common.JsonEquivalent
import com.thintwice.archive.mbompay.domain.common.ProvidersType
import java.util.*

data class ProviderInput(
    val id: UUID? = null,
    val name: String? = null,
    val type: ProvidersType? = null,
    val marks: List<String>? = null,
    val logo: UUID? = null,
) : JsonEquivalent