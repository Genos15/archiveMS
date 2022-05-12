package com.thintwice.archive.user.domain.input

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.thintwice.archive.user.configurations.object_mapping.JSON
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserInput(
    val id: UUID,
    val firstname: String? = null,
    val lastname: String? = null,
    val photo: String? = null,
) : JsonEquivalent {
    override fun toJson(): String = JSON.parser.toJsonString(this)
}