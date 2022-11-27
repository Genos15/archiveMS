package com.thintwice.archive.mbompay.domain.model

import java.util.*

data class Mode(
    val id: UUID,
    val name: String,
    val country: Country? = null,
    val marks: List<String> = emptyList(),
    val logo: UUID? = null,
)