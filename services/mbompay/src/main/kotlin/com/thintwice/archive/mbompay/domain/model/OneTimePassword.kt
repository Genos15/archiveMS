package com.thintwice.archive.mbompay.domain.model

import com.thintwice.archive.mbompay.domain.common.JsonEquivalent
import java.util.*

data class OneTimePassword(
    val id: UUID,
    val code: String,
    val expiredAt: Long,
    val ownerContact: String,
    val ownerName: String? = null,
    val locale: String = Locale.ENGLISH.language,
) : JsonEquivalent