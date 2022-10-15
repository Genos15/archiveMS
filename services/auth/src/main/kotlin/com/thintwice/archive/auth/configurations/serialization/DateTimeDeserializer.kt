package com.thintwice.archive.auth.configurations.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalQueries

object DateTimeDeserializer : JsonDeserializer<OffsetDateTime>() {
    override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext?): OffsetDateTime {
        val string = jsonParser.text
        val temporal: TemporalAccessor = DateTimeFormatter.ISO_DATE_TIME.parseBest(string,
            { temporal: TemporalAccessor? -> OffsetDateTime.from(temporal) },
            LocalDateTime::from)
        return if (temporal.query(TemporalQueries.offset()) == null) {
            LocalDateTime.from(temporal).atOffset(ZoneOffset.UTC)
        } else {
            OffsetDateTime.from(temporal)
        }
    }
}