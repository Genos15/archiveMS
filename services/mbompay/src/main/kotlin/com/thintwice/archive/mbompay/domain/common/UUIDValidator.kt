package com.thintwice.archive.mbompay.domain.common

import java.util.*

object UUIDValidator {
    fun isValidUUID(uuid: String): Boolean = try {
        UUID.fromString(uuid)
        true
    } catch (e: Exception) {
        false
    }
}