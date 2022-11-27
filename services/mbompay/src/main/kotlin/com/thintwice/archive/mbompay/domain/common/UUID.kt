package com.thintwice.archive.mbompay.domain.common

import java.util.UUID

fun uUIDStringOrNull(input: String?) =  try {
    UUID.fromString(input)
}catch (e: IllegalArgumentException) {
    null
}