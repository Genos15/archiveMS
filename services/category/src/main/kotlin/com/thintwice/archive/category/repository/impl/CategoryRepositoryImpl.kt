package com.thintwice.archive.category.repository.impl

import com.thintwice.archive.category.domain.input.CategoryInput
import com.thintwice.archive.category.domain.mapper.CategoryMapper
import com.thintwice.archive.category.domain.model.Category
import com.thintwice.archive.category.domain.sql_queries.SourceQuery
import com.thintwice.archive.category.repository.CategoryRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Component
import java.util.*

@Component
class CategoryRepositoryImpl(
    private val database: DatabaseClient,
    private val mapper: CategoryMapper,
    private val logger: KLogger = KotlinLogging.logger {},
) : CategoryRepository {

    override suspend fun create(input: CategoryInput): Optional<Category> = database.sql(SourceQuery.create)
        .bind("input", input.toJson())
        .map(mapper::asSingle)
        .first()
        .doOnError { logger.error { it.message } }
        .log()
        .awaitFirstOrElse { Optional.empty() }

    override suspend fun delete(id: UUID): Boolean = database.sql(SourceQuery.delete)
        .bind("id", id)
        .fetch()
        .first()
        .map { it.values.firstOrNull() as Boolean? ?: false }
        .doOnError { logger.error { it.message } }
        .log()
        .awaitFirstOrElse { false }

    override suspend fun find(id: UUID): Optional<Category> = database.sql(SourceQuery.find)
        .bind("id", id)
        .map(mapper::asSingle)
        .first()
        .doOnError { logger.error { it.message } }
        .log()
        .awaitFirstOrElse { Optional.empty() }

    override suspend fun retrieve(first: Int, after: UUID?): List<Category> = database.sql(SourceQuery.retrieve)
        .bind("first", first)
        .bind("after", after)
        .map(mapper::asList)
        .first()
        .doOnError { logger.error { it.message } }
        .log()
        .awaitFirstOrElse { emptyList() }

    override suspend fun search(text: String, first: Int, after: UUID?): List<Category> =
        database.sql(SourceQuery.search)
            .bind("text", text)
            .bind("first", first)
            .bind("after", after)
            .map(mapper::asList)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyList() }

    override suspend fun count(): Long = database.sql(SourceQuery.count)
        .fetch()
        .first()
        .map { it.values.firstOrNull() as Long? ?: 0 }
        .doOnError { logger.error { it.message } }
        .log()
        .awaitFirstOrElse { 0 }
}