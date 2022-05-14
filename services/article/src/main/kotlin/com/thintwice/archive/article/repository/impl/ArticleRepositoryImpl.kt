package com.thintwice.archive.article.repository.impl

import com.thintwice.archive.article.domain.input.ArticleInput
import com.thintwice.archive.article.domain.mapper.ArticleMapper
import com.thintwice.archive.article.domain.model.Article
import com.thintwice.archive.article.domain.sql_queries.SourceQuery
import com.thintwice.archive.article.repository.ArticleRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Component
import java.util.*

@Component
class ArticleRepositoryImpl(
    private val database: DatabaseClient,
    private val mapper: ArticleMapper,
    private val logger: KLogger = KotlinLogging.logger {},
) : ArticleRepository {

    override suspend fun create(input: ArticleInput, token: UUID): Optional<Article> = database.sql(SourceQuery.create)
        .bind("input", input.toJson())
        .bind("token", token)
        .map(mapper::asSingle)
        .first()
        .doOnError { logger.error { it.message } }
        .log()
        .awaitFirstOrElse { Optional.empty() }

    override suspend fun delete(id: UUID, token: UUID): Boolean = database.sql(SourceQuery.delete)
        .bind("id", id)
        .bind("token", token)
        .fetch()
        .first()
        .map { it.values.firstOrNull() as Boolean? ?: false }
        .doOnError { logger.error { it.message } }
        .log()
        .awaitFirstOrElse { false }

    override suspend fun sell(id: UUID, token: UUID): Optional<Article> = database.sql(SourceQuery.sell)
        .bind("id", id)
        .bind("token", token)
        .map(mapper::asSingle)
        .first()
        .doOnError { logger.error { it.message } }
        .log()
        .awaitFirstOrElse { Optional.empty() }

    override suspend fun wishlist(first: Int, after: UUID?, token: UUID): List<Article> =
        database.sql(SourceQuery.wishlist)
            .bind("first", first)
            .bind("after", after)
            .bind("token", token)
            .map(mapper::asList)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyList() }

    override suspend fun removeInWishlist(id: UUID, token: UUID): Boolean = database.sql(SourceQuery.removeInWishlist)
        .bind("id", id)
        .bind("token", token)
        .fetch()
        .first()
        .map { it.values.firstOrNull() as Boolean? ?: false }
        .doOnError { logger.error { it.message } }
        .log()
        .awaitFirstOrElse { false }

    override suspend fun addInWishlist(id: UUID, token: UUID): Optional<Article> =
        database.sql(SourceQuery.addInWishlist)
            .bind("id", id)
            .bind("token", token)
            .map(mapper::asSingle)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { Optional.empty() }

    override suspend fun categoryArticles(id: UUID, first: Int, after: UUID?, token: UUID): List<Article> =
        database.sql(SourceQuery.categoryArticles)
            .bind("first", first)
            .bind("after", after)
            .bind("token", token)
            .bind("id", id)
            .map(mapper::asList)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyList() }

    override suspend fun userArticles(id: UUID, first: Int, after: UUID?, token: UUID): List<Article> =
        database.sql(SourceQuery.userArticles)
            .bind("first", first)
            .bind("after", after)
            .bind("token", token)
            .bind("id", id)
            .map(mapper::asList)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyList() }

    override suspend fun myArticles(first: Int, after: UUID?, token: UUID): List<Article> =
        database.sql(SourceQuery.myArticles)
            .bind("first", first)
            .bind("after", after)
            .bind("token", token)
            .map(mapper::asList)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyList() }

    override suspend fun search(text: String, first: Int, after: UUID?, token: UUID): List<Article> =
        database.sql(SourceQuery.search)
            .bind("first", first)
            .bind("after", after)
            .bind("token", token)
            .bind("text", text)
            .map(mapper::asList)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyList() }

    override suspend fun retrieve(first: Int, after: UUID?, token: UUID): List<Article> =
        database.sql(SourceQuery.retrieve)
            .bind("first", first)
            .bind("after", after)
            .bind("token", token)
            .map(mapper::asList)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyList() }

    override suspend fun article(id: UUID, token: UUID): Optional<Article> = database.sql(SourceQuery.find)
        .bind("id", id)
        .bind("token", token)
        .map(mapper::asSingle)
        .first()
        .doOnError { logger.error { it.message } }
        .log()
        .awaitFirstOrElse { Optional.empty() }

}