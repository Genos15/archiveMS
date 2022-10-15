package com.thintwice.archive.mbompay.domain.common

interface JsonEquivalent {
    fun toJson(): String = JSON.parser.toJsonString(this)
}