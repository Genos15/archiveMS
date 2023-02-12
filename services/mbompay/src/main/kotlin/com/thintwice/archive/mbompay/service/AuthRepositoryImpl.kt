package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.configuration.database.DbClient
import com.thintwice.archive.mbompay.domain.common.jsonOf
import com.thintwice.archive.mbompay.domain.common.jsonOfOrNull
import com.thintwice.archive.mbompay.domain.common.parameterOrNull
import com.thintwice.archive.mbompay.domain.input.LocalizationInput
import com.thintwice.archive.mbompay.domain.input.UserInput
import com.thintwice.archive.mbompay.domain.mapper.SessionTokenMapper
import com.thintwice.archive.mbompay.domain.model.SessionToken
import com.thintwice.archive.mbompay.repository.AuthRepository
import mu.KLogger
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.*
import kotlinx.coroutines.reactive.awaitFirstOrElse


@Service
class AuthRepositoryImpl(
    private val qr: RB,
    private val dbClient: DbClient,
    private val mapper: SessionTokenMapper,
    private val logger: KLogger = KotlinLogging.logger {},
) : AuthRepository {
    override suspend fun login(imei: String, where: LocalizationInput?): Optional<SessionToken> {
        return dbClient.exec("SELECT users_on_auth(imei := :imei, my_where := :where)")
            .bind("imei", imei)
            .bind("where", jsonOfOrNull(where))
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun refresh(token: UUID, where: LocalizationInput?): Optional<SessionToken> {
        return dbClient.exec("SELECT users_on_refresh_token(token := :token, my_where := :where)")
            .bind("token", token)
            .bind("where", jsonOfOrNull(where))
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun connect(input: UserInput, code: String?): Optional<String> {
        val query = qr.l("mutation.connect.user")
        return dbClient.exec(query = query)
            .bind("input", jsonOf(input))
            .bind("code", parameterOrNull(code))
            .fetch()
            .first()
            .map { Optional.ofNullable(it.values.firstOrNull() as String?)  }
            .doOnError { logger.error { it.message } }
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun deleteUser(id: UUID, token: UUID): Boolean {
        val query = qr.l("mutation.delete.user")
        return dbClient.exec(query = query)
            .bind("id", id)
            .bind("token", token)
            .fetch()
            .first()
            .map { it.values.firstOrNull() as Boolean? ?: false }
            .doOnError { logger.error { it.message } }
            .awaitFirstOrElse { false }
    }

}