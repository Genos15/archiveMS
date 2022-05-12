package com.thintwice.archive.model.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.UUID

@Table(value = "roles")
data class Role(
    @Id val id: UUID,
    val name: String,
    val displayName: String,
    val description: String? = null,
    val createdAt: OffsetDateTime,
    val editedAt: OffsetDateTime
)