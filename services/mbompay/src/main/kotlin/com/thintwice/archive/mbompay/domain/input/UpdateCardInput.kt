package com.thintwice.archive.mbompay.domain.input

import com.thintwice.archive.mbompay.domain.common.JsonEquivalent
import java.util.*

data class UpdateCardInput(
    val id: UUID,
    val isDefault: Boolean? = null,
) : JsonEquivalent