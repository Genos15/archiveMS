package com.thintwice.archive.article.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.*

@Table(value = "articles")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Article(
    @Id val id: UUID,
    val name: String,
    val model: String? = null,
    val series: String? = null,
    val brand: String? = null,
    val description: String,
    val price: Long,
    val isSold: Boolean,
    val citiesAvailability: List<String>,
    val images: List<UUID>,
    val createdAt: OffsetDateTime,
    val editedAt: OffsetDateTime,
)