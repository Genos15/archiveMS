package com.thintwice.archive.auth.domain.mapper

import com.thintwice.archive.auth.domain.model.Token
import io.r2dbc.spi.Row
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thintwice.archive.auth.configurations.object_mapping.MapperState
import com.thintwice.archive.auth.exceptions.failOnNull
import mu.KLogger
import mu.KotlinLogging
import java.util.*

@Component
class TokenMapper(
    private val logger: KLogger = KotlinLogging.logger {},
    private val mapper: ObjectMapper,
) : MapperState<Row, Any, Token> {

    override fun asList(row: Row, o: Any): List<Token> = try {
        val payload = row.get(0, String::class.java) ?: "{}" ?: failOnNull()
        mapper.readValue(payload)
    } catch (e: Exception) {
        logger.warn { e.message }
        emptyList()
    }

    override fun asSingle(row: Row, o: Any): Optional<Token> = try {
        val payload = row.get(0, String::class.java) ?: "{}" ?: failOnNull()
        Optional.ofNullable(mapper.readValue<List<Token>?>(payload)?.firstOrNull())
    } catch (e: Exception) {
        logger.warn { e.message }
        Optional.empty<Token>()
    }
}