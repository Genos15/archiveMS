package com.thintwice.archive.mbompay.domain.input

import com.thintwice.archive.mbompay.domain.common.JsonEquivalent
import java.util.*

data class LocalizationInput(val longitude: Float? = null, val latitude: Float? = null, val language: String? = null) :
    JsonEquivalent