package com.thintwice.archive.user.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.*

@Table(value = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
    @Id val id: UUID,
    val firstname: String? = null,
    val lastname: String? = null,
    val phone: String,
    val photo: String? = null,
    val createdAt: OffsetDateTime,
    val editedAt: OffsetDateTime,
)