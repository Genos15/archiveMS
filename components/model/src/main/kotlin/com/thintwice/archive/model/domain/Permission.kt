package com.thintwice.archive.model.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.UUID

@Table(value = "permissions")
data class Permission(
    @Id val id: UUID,
    val name: String,
    val description: String? = null,
    val createdAt: OffsetDateTime,
    val editedAt: OffsetDateTime
)