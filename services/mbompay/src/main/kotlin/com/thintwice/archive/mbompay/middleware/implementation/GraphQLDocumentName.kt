package com.thintwice.archive.mbompay.middleware.implementation

import org.springframework.graphql.server.WebGraphQlRequest

class GraphQLDocumentName(private val request: WebGraphQlRequest) {
    operator fun invoke(): String? {
        val document = request.document
            .split("\n")
            .filterNot { it.contains("#") }
            .filter { it.isNotEmpty() }
            .joinToString(separator = "")

        return try {
            val documentLocationNameIndex = if (document.split("(").size > 2) 2
            else 1

            document.trim { it <= ' ' }
                .replace("{", "∞")
                .replace("(", "∞")
                .replace("__typename", "")
                .split("∞")
                .getOrNull(documentLocationNameIndex)?.trim { it <= ' ' }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}