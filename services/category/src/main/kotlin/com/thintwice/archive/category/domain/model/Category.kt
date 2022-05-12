package com.thintwice.archive.category.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.*

@Table(value = "categories_article")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Category(
    @Id val id: UUID,
    val name: String,
    val description: String? = null,
    val ioniconsName: String? = null,
    val defaultIconName: String? = null,
    val createdAt: OffsetDateTime,
    val editedAt: OffsetDateTime,
)