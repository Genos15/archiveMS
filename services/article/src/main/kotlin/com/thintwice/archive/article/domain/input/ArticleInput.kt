package com.thintwice.archive.article.domain.input

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.thintwice.archive.article.configurations.object_mapping.JSON

@JsonIgnoreProperties(ignoreUnknown = true)
data class ArticleInput(
    val id: String? = null,
    val name: String? = null,
    val model: String? = null,
    val series: String? = null,
    val brand: String? = null,
    val description: String? = null,
    val price: Long? = null,
    val citiesAvailability: List<String>? = null,
    val images: List<String>? = null,
    val categoryId: String? = null,
    val discountId: String? = null,
) : JsonEquivalent {
    override fun toJson(): String = JSON.parser.toJsonString(this)
}
