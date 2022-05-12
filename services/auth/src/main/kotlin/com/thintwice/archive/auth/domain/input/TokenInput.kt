package com.thintwice.archive.auth.domain.input

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.thintwice.archive.auth.configurations.object_mapping.JSON

@JsonIgnoreProperties(ignoreUnknown = true)
data class TokenInput(
    val phone: String,
    val code: String? = null,
) : JsonEquivalent {
    override fun toJson(): String = JSON.parser.toJsonString(this)
}