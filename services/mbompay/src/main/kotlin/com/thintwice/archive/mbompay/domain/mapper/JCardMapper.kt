package com.thintwice.archive.mbompay.domain.mapper

import io.r2dbc.spi.Row
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thintwice.archive.mbompay.domain.common.MapperState
import com.thintwice.archive.mbompay.domain.model.JCard
import java.util.*

@Component
class JCardMapper(
    private val mapper: ObjectMapper,
) : MapperState<Row, Any, JCard> {

    override fun list(row: Row, o: Any): List<JCard> = runCatching {
        val payload = row.get(0, String::class.java) ?: error("something went wrong with mapping values")
        mapper.readValue<List<JCard>>(payload)
    }.recover {
        println("-- JCardMapper Error = $it")
        emptyList()
    }.getOrDefault(emptyList())

    override fun factory(row: Row, o: Any): Optional<JCard> = runCatching {
        val payload = row.get(0, String::class.java) ?: error("something went wrong with mapping value")
        Optional.ofNullable(mapper.readValue<List<JCard>?>(payload)?.firstOrNull())
    }.recover {
        println("-- JCardMapper Error = $it")
        Optional.empty()
    }.getOrDefault(Optional.empty())
}