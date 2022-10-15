package com.thintwice.archive.user.repository.impl

import com.thintwice.archive.user.domain.input.UserInput
import com.thintwice.archive.user.domain.mapper.UserMapper
import com.thintwice.archive.user.domain.model.User
import com.thintwice.archive.user.domain.sql_queries.SourceQuery
import com.thintwice.archive.user.repository.UserRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserRepositoryImpl(
    private val database: DatabaseClient,
    private val mapper: UserMapper,
    private val logger: KLogger = KotlinLogging.logger {},
) : UserRepository {

    override suspend fun edit(input: UserInput, token: UUID): Optional<User> = database.sql(SourceQuery.edit)
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

    override suspend fun user(id: UUID?, token: UUID): Optional<User> = database.sql(SourceQuery.find)
        .bind("id", id)
        .bind("token", token)
        .map(mapper::asSingle)
        .first()
        .doOnError { logger.error { it.message } }
        .log()
        .awaitFirstOrElse { Optional.empty() }

}