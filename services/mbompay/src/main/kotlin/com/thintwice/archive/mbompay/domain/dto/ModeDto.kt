package com.thintwice.archive.mbompay.domain.dto

import com.thintwice.archive.mbompay.domain.model.Country
import com.thintwice.archive.mbompay.domain.model.Mode
import java.util.*

data class ModeDto(
    val id: UUID,
    val name: String,
    val country: Country? = null,
    val marks: List<String> = emptyList(),
) {
    val mode: Mode
        get() = Mode(id = id, name = name, country = country, marks = marks)
}