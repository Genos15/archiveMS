package com.thintwice.archive.mbompay.domain.model

import com.thintwice.archive.mbompay.domain.common.JsonEquivalent

data class JwtToken(val accessToken: String, val refreshToken: String, val expiredAt: Long, val userRole: String? = null) :
    JsonEquivalent