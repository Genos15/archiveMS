package com.thintwice.archive.mbompay.domain.common

import io.r2dbc.postgresql.codec.Json
import org.springframework.r2dbc.core.Parameter

fun jsonOfOrNull(element: JsonEquivalent?): Parameter =
    Parameter.fromOrEmpty(element?.toJson()?.let { Json.of(it) }, Json::class.java)

fun jsonOf(element: JsonEquivalent): Json = Json.of(element.toJson())

inline fun <reified T>parameterOrNull(element: T?): Parameter =
    Parameter.fromOrEmpty(element, T::class.java)