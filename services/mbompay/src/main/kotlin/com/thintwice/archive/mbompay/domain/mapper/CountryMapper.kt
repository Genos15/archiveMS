package com.thintwice.archive.mbompay.domain.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thintwice.archive.mbompay.domain.common.MapperState
import com.thintwice.archive.mbompay.domain.dto.CountryDto
import com.thintwice.archive.mbompay.domain.model.Country
import io.r2dbc.spi.Row
import mu.KLogger
import mu.KotlinLogging
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.stereotype.Component
import java.util.*

@Component
class CountryMapper(
    private val logger: KLogger = KotlinLogging.logger {},
    private val mapper: ObjectMapper,
) : MapperState<Row, Any, Country> {

    fun getListDto(enMap: JSONArray, vararg otherLangMap: JSONObject): Iterable<CountryDto> {
        val countries = enMap.filterIsInstance<JSONObject>().map {
            CountryDto(
                id = UUID.randomUUID(),
                unicode = it.getString("unicode"),
                flag = it.getString("flag"),
                nameEn = it.getString("name"),
                iso = it.getString("iso"),
            )
        }.associateBy({ it.iso }, { it })

        for (countryMap in otherLangMap.map { it.toMap() }) {
            countryMap.forEach {
                countries[it.key]?.nameFr = it.value as String?
            }
        }

        return countries.values
    }

    override fun list(row: Row, o: Any): List<Country> = try {
        val payload = row.get(0, String::class.java) ?: error("something went wrong with mapping values")
        mapper.readValue(payload)
    } catch (e: Exception) {
        emptyList()
    }

    override fun factory(row: Row, o: Any): Optional<Country> = try {
        val payload = row.get(0, String::class.java) ?: error("something went wrong with mapping value")
        Optional.ofNullable(mapper.readValue<List<Country>?>(payload)?.firstOrNull())
    } catch (e: Exception) {
        Optional.empty<Country>()
    }

}