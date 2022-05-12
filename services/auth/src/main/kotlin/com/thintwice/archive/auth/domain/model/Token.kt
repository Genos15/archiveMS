package com.thintwice.archive.auth.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(value = "tokens")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Token(
    @Id val id: UUID,
    val accessToken: UUID,
    val refreshToken: UUID,
    val verified: Boolean,
    val expiredIn: Long,
)