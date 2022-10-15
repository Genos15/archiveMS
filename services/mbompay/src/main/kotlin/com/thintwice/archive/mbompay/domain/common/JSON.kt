package com.thintwice.archive.mbompay.domain.common

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.Klaxon
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.UUID


private val OffsetDateTimeConverter = object : Converter {
    override fun canConvert(cls: Class<*>) = cls == OffsetDateTime::class.java

    override fun toJson(value: Any): String = "\"${"$value".replace(",", "")}\""

    override fun fromJson(jv: JsonValue): OffsetDateTime {
        val datetime = LocalDateTime.parse(jv.string)
        return datetime.atZone(ZoneId.of("Europe/Paris")).toOffsetDateTime()
    }
}

private val UUIDConverter = object : Converter {
    override fun canConvert(cls: Class<*>) = cls == UUID::class.java

    override fun toJson(value: Any): String = "\"${UUID.fromString("$value")}\""

    override fun fromJson(jv: JsonValue): UUID = UUID.fromString(jv.string)
}

object JSON {
    val parser: Klaxon
        get() = Klaxon().converter(converter = OffsetDateTimeConverter).converter(converter = UUIDConverter)
}