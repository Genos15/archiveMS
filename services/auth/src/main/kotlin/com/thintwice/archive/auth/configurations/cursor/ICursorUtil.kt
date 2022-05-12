package com.thintwice.archive.auth.configurations.cursor


import graphql.relay.ConnectionCursor
import graphql.relay.DefaultConnectionCursor
import graphql.relay.Edge
import java.nio.charset.StandardCharsets
import java.util.*

interface ICursorUtil {

    fun createCursorWith(id: UUID? = null): ConnectionCursor {
        return DefaultConnectionCursor(
            Base64.getEncoder().encodeToString(id?.toString()?.toByteArray(StandardCharsets.UTF_8) ?: byteArrayOf())
        )
    }

    fun decode(cursor: String): UUID {
        return UUID.fromString(String(Base64.getDecoder().decode(cursor)))
    }

    fun <T> firstCursorFrom(edges: List<Edge<T>>): ConnectionCursor? = if (edges.isEmpty()) {
        null
    } else {
        edges.first().cursor
    }

    fun <T> lastCursorFrom(edges: List<Edge<T>>): ConnectionCursor? = if (edges.isEmpty()) {
        null
    } else {
        edges.last().cursor
    }
}