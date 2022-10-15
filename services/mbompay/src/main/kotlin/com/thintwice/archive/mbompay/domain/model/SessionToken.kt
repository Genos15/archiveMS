package com.thintwice.archive.mbompay.domain.model

import java.util.*

class SessionToken(
    val id: UUID,
    val accessToken: UUID,
    val refreshToken: UUID,
    val expiredIn: Long,
    val verified: Boolean,
    val lang: String?,
    val longitude: Float?,
    val latitude: Float?,
)

