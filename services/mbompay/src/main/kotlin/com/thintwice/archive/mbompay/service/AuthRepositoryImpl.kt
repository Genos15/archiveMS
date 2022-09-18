package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.domain.common.jsonOfOrNull
import com.thintwice.archive.mbompay.domain.input.LocalizationInput
import com.thintwice.archive.mbompay.domain.mapper.SessionTokenMapper
import com.thintwice.archive.mbompay.domain.model.SessionToken
import com.thintwice.archive.mbompay.repository.AuthRepository
import io.r2dbc.postgresql.codec.Json
import mu.KLogger
import mu.KotlinLogging
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Service
import java.util.*
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.r2dbc.core.Parameter


@Service
class AuthRepositoryImpl(
    private val database: DatabaseClient,
    private val mapper: SessionTokenMapper,
    private val logger: KLogger = KotlinLogging.logger {},
) : AuthRepository {
    override suspend fun login(imei: String, where: LocalizationInput?): Optional<SessionToken> {
        return database.sql("SELECT users_on_auth(imei := :imei, my_where := :where)")
            .bind("imei", imei)
            .bind("where", Parameter.fromOrEmpty(jsonOfOrNull(where), Json::class.java))
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun refresh(token: UUID, where: LocalizationInput?): Optional<SessionToken> {
        return database.sql("SELECT users_on_refresh_token(token := :token, my_where := :where)")
            .bind("token", token)
            .bind("where", Parameter.fromOrEmpty(jsonOfOrNull(where), Json::class.java))
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

}