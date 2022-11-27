package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.configuration.database.DbClient
import com.thintwice.archive.mbompay.domain.common.jsonOf
import com.thintwice.archive.mbompay.domain.common.parameterOrNull
import com.thintwice.archive.mbompay.domain.input.OperatorInput
import com.thintwice.archive.mbompay.domain.mapper.OperatorMapper
import com.thintwice.archive.mbompay.domain.model.Operator
import com.thintwice.archive.mbompay.repository.OperatorRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.r2dbc.core.Parameter
import org.springframework.stereotype.Service
import java.util.*

@Service
class OperatorRepositoryImpl(
    private val qr: RB,
    private val dbClient: DbClient,
    private val mapper: OperatorMapper,
    private val logger: KLogger = KotlinLogging.logger {},
) : OperatorRepository {
    override suspend fun operator(input: OperatorInput, token: UUID): Optional<Operator> {
        val query = qr.l("mutation.create.edit.operator")
        return dbClient.exec(query = query)
            .bind("input", jsonOf(input))
            .bind("token", token)
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun operator(id: UUID, token: UUID): Optional<Operator> {
        val query = qr.l("query.find.operator")
        return dbClient.exec(query = query)
            .bind("id", id)
            .bind("token", token)
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun operators(first: Int, after: UUID?, token: UUID): Iterable<Operator> {
        val query = qr.l("query.retrieve.operator")
        return dbClient.exec(query = query)
            .bind("first", first)
            .bind("after", parameterOrNull(after))
            .bind("token", token)
            .map(mapper::list)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyList() }
    }

}