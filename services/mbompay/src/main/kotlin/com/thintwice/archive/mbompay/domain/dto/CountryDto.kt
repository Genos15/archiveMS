package com.thintwice.archive.mbompay.domain.dto

import com.thintwice.archive.mbompay.domain.common.JsonEquivalent
import com.thintwice.archive.mbompay.domain.model.Country
import java.util.*

data class CountryDto(
    val id: UUID,
    val unicode: String,
    val flag: String,
    val iso: String,
    val nameEn: String,
    var nameFr: String? = null,
) : JsonEquivalent {
    val country: Country
        get() = Country(id = id, unicode = unicode, flag = flag, iso = iso, nameEn = nameEn, nameFr = nameFr)
}