package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.configuration.database.DbClient
import com.thintwice.archive.mbompay.domain.common.jsonOf
import com.thintwice.archive.mbompay.domain.input.UserDetailInput
import com.thintwice.archive.mbompay.domain.mapper.UserDetailMapper
import com.thintwice.archive.mbompay.domain.model.UserDetail
import com.thintwice.archive.mbompay.repository.UserRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserRepositoryImpl(
    private val qr: RB,
    private val dbClient: DbClient,
    private val mapper: UserDetailMapper,
    private val logger: KLogger = KotlinLogging.logger {},
) : UserRepository {
    override suspend fun userDetail(input: UserDetailInput): Optional<UserDetail> {
        val query = qr.l("mutation.create.edit.user.detail")
        return dbClient.exec(query = query)
            .bind("input", jsonOf(input))
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

}