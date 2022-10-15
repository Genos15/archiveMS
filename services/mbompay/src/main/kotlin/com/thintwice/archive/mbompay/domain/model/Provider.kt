package com.thintwice.archive.mbompay.domain.model

import com.thintwice.archive.mbompay.domain.common.ProvidersType
import java.util.*

data class Provider(
    val id: UUID,
    val name: String,
    val type: ProvidersType,
    val marks: List<String>? = emptyList(),
    val logo: UUID? = null,
)