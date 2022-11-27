package com.thintwice.archive.mbompay.domain.model

import java.util.*

data class Country(
    val id: UUID,
    val unicode: String,
    val flag: String,
    val iso: String,
    val nameEn: String,
    var nameFr: String? = null,
    val modes: List<Mode>? = emptyList(),
)