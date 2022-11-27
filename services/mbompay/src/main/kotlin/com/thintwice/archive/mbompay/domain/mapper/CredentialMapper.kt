package com.thintwice.archive.mbompay.domain.mapper

import io.r2dbc.spi.Row
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thintwice.archive.mbompay.domain.common.MapperState
import com.thintwice.archive.mbompay.domain.model.Credential
import com.thintwice.archive.mbompay.domain.model.UserCredential
import mu.KLogger
import mu.KotlinLogging
import java.util.*

@Component
class CredentialMapper(
    private val logger: KLogger = KotlinLogging.logger {},
    private val mapper: ObjectMapper,
) : MapperState<Row, Any, UserCredential> {

    override fun list(row: Row, o: Any): List<UserCredential> = try {
        val payload = row.get(0, String::class.java) ?: error("something went wrong with mapping values")
        mapper.readValue(payload)
    } catch (e: Exception) {
        logger.warn { e.message }
        emptyList()
    }

    override fun factory(row: Row, o: Any): Optional<UserCredential> = try {
        val payload = row.get(0, String::class.java) ?: error("something went wrong with mapping value")
        Optional.ofNullable(mapper.readValue<List<UserCredential>?>(payload)?.firstOrNull())
    } catch (e: Exception) {
        logger.warn { e.message }
        Optional.empty<UserCredential>()
    }

    fun one(row: Row, o: Any): Optional<Credential> = try {
        val payload = row.get(0, String::class.java) ?: error("something went wrong with mapping value")
        Optional.ofNullable(mapper.readValue<List<UserCredential>?>(payload)?.firstOrNull())
    } catch (e: Exception) {
        logger.warn { e.message }
        Optional.empty<Credential>()
    }
}