package com.thintwice.archive.article.domain.mapper

import com.thintwice.archive.article.domain.model.Article
import io.r2dbc.spi.Row
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thintwice.archive.article.configurations.object_mapping.MapperState
import mu.KLogger
import mu.KotlinLogging
import java.util.*

@Component
class ArticleMapper(
    private val logger: KLogger = KotlinLogging.logger {},
    private val mapper: ObjectMapper,
) : MapperState<Row, Any, Article> {

    override fun asList(row: Row, o: Any): List<Article> = try {
        val payload = row.get(0, String::class.java) ?: error("something went wrong with mapping value")
        mapper.readValue(payload)
    } catch (e: Exception) {
        logger.warn { e.message }
        emptyList()
    }

    override fun asSingle(row: Row, o: Any): Optional<Article> = try {
        val payload = row.get(0, String::class.java) ?: error("something went wrong with mapping value")
        Optional.ofNullable(mapper.readValue<List<Article>?>(payload)?.firstOrNull())
    } catch (e: Exception) {
        logger.warn { e.message }
        Optional.empty<Article>()
    }
}