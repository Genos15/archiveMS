package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.configuration.database.DbClient
import com.thintwice.archive.mbompay.domain.common.jsonOf
import com.thintwice.archive.mbompay.domain.common.parameterOrNull
import com.thintwice.archive.mbompay.domain.input.AdminInput
import com.thintwice.archive.mbompay.domain.mapper.AdminMapper
import com.thintwice.archive.mbompay.domain.model.Admin
import com.thintwice.archive.mbompay.repository.AdminRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.*

@Service
class AdminRepositoryImpl(
    private val qr: RB,
    private val dbClient: DbClient,
    private val mapper: AdminMapper,
    private val logger: KLogger = KotlinLogging.logger {},
) : AdminRepository {

    override suspend fun admin(input: AdminInput, token: UUID): Optional<Admin> {
        val query = qr.l("mutation.create.edit.admin")
        return dbClient.exec(query = query)
            .bind("input", jsonOf(input))
            .bind("token", token)
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun admin(id: UUID, token: UUID): Optional<Admin> {
        val query = qr.l("query.find.admin")
        return dbClient.exec(query = query)
            .bind("id", id)
            .bind("token", token)
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun admins(first: Int, after: UUID?, token: UUID): Iterable<Admin> {
        val query = qr.l("query.retrieve.admin")
        return dbClient.exec(query = query)
            .bind("first", first)
            .bind("after", parameterOrNull(after))
            .bind("token", token)
            .map(mapper::list)
            .first()
            .doOnError { logger.error { it.message } }
            .awaitFirstOrElse { emptyList() }
    }

}