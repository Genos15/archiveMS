package com.thintwice.archive.category.domain.input

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.thintwice.archive.category.configurations.object_mapping.JSON
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class CategoryInput(
    val id: UUID? = null,
    val name: String? = null,
    val description: String? = null,
    val ioniconsName: String? = null,
    val defaultIconName: String? = null,
) : JsonEquivalent {
    override fun toJson(): String = JSON.parser.toJsonString(this)
}