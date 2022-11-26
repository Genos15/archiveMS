package com.thintwice.archive.mbompay.domain.mapper

import io.r2dbc.spi.Row
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thintwice.archive.mbompay.domain.common.MapperState
import com.thintwice.archive.mbompay.domain.dto.CustomerDto
import com.thintwice.archive.mbompay.domain.model.Customer
import mu.KLogger
import mu.KotlinLogging
import java.util.*

@Component
class CustomerMapper(
    private val logger: KLogger = KotlinLogging.logger {},
    private val mapper: ObjectMapper,
) : MapperState<Row, Any, Customer> {

    override fun list(row: Row, o: Any): List<Customer> = try {
        val payload = row.get(0, String::class.java) ?: error("something went wrong with mapping values")
        mapper.readValue(payload)
    } catch (e: Exception) {
        logger.warn { e.message }
        emptyList()
    }

    override fun factory(row: Row, o: Any): Optional<Customer> = try {
        val payload = row.get(0, String::class.java) ?: error("something went wrong with mapping value")
        Optional.ofNullable(mapper.readValue<List<Customer>?>(payload)?.firstOrNull())
    } catch (e: Exception) {
        logger.warn { e.message }
        Optional.empty<Customer>()
    }

    fun factoryDto(row: Row, o: Any): CustomerDto? = try {
        val payload = row.get(0, String::class.java) ?: error("something went wrong with mapping value")
        mapper.readValue<List<CustomerDto>?>(payload)?.firstOrNull()
    } catch (e: Exception) {
        logger.warn { e.message }
        null
    }
}