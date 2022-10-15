package com.thintwice.archive.file.configurations

import java.util.*

fun String.toUuid(): UUID = try {
    UUID.fromString(this)
} catch (e: Exception) {
    throw IllegalArgumentException("Invalid id")
}