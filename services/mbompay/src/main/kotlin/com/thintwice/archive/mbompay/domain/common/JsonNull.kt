package com.thintwice.archive.mbompay.domain.common

import io.r2dbc.postgresql.codec.Json

fun jsonOfOrNull(element: JsonEquivalent?): Json? = element?.toJson()?.let { Json.of(it) }
fun jsonOf(element: JsonEquivalent): Json = Json.of(element.toJson())