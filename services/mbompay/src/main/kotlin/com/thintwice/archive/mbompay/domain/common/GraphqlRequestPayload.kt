package com.thintwice.archive.mbompay.domain.common

import java.util.Locale

data class GraphqlRequestPayload(
    val document: String,
    val operationName: String? = null,
    val id: String,
    val locale: Locale? = null,
) : Any()