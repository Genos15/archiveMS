package com.thintwice.archive.user.domain.mapper

import com.thintwice.archive.user.domain.model.User
import io.r2dbc.spi.Row
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thintwice.archive.user.configurations.object_mapping.MapperState
import mu.KLogger
import mu.KotlinLogging
import java.util.*

@Component
class UserMapper(
    private val logger: KLogger = KotlinLogging.logger {},
    private val mapper: ObjectMapper,
) : MapperState<Row, Any, User> {

    override fun asList(row: Row, o: Any): List<User> = try {
        val payload = row.get(0, String::class.java) ?: error("something went wrong with mapping value")
        mapper.readValue(payload)
    } catch (e: Exception) {
        logger.warn { e.message }
        emptyList()
    }

    override fun asSingle(row: Row, o: Any): Optional<User> = try {
        val payload = row.get(0, String::class.java) ?: error("something went wrong with mapping value")
        Optional.ofNullable(mapper.readValue<List<User>?>(payload)?.firstOrNull())
    } catch (e: Exception) {
        logger.warn { e.message }
        Optional.empty<User>()
    }
}