package com.thintwice.archive.category.domain.mapper

import com.thintwice.archive.category.domain.model.Category
import io.r2dbc.spi.Row
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thintwice.archive.category.configurations.object_mapping.MapperState
import com.thintwice.archive.category.exceptions.failOnNull
import mu.KLogger
import mu.KotlinLogging
import java.util.*

@Component
class CategoryMapper(
    private val logger: KLogger = KotlinLogging.logger {},
    private val mapper: ObjectMapper,
) : MapperState<Row, Any, Category> {

    override fun asList(row: Row, o: Any): List<Category> = try {
        val payload = row.get(0, String::class.java) ?: "{}" ?: failOnNull()
        mapper.readValue(payload)
    } catch (e: Exception) {
        logger.warn { e.message }
        emptyList()
    }

    override fun asSingle(row: Row, o: Any): Optional<Category> = try {
        val payload = row.get(0, String::class.java) ?: "{}" ?: failOnNull()
        Optional.ofNullable(mapper.readValue<List<Category>?>(payload)?.firstOrNull())
    } catch (e: Exception) {
        logger.warn { e.message }
        Optional.empty<Category>()
    }
}