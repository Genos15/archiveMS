package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.configuration.database.DbClient
import com.thintwice.archive.mbompay.domain.common.jsonOf
import com.thintwice.archive.mbompay.domain.common.parameterOrNull
import com.thintwice.archive.mbompay.domain.dto.CountryDto
import com.thintwice.archive.mbompay.domain.mapper.CountryMapper
import com.thintwice.archive.mbompay.domain.mapper.ModeMapper
import com.thintwice.archive.mbompay.domain.model.Country
import com.thintwice.archive.mbompay.domain.model.Mode
import com.thintwice.archive.mbompay.repository.CountryRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.reactive.awaitFirstOrElse
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import mu.KLogger
import mu.KotlinLogging
import org.apache.commons.io.IOUtils
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.core.awaitRowsUpdated
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.nio.charset.StandardCharsets
import java.util.*

@Service
class CountryRepositoryImpl(
    private val dbClient: DbClient,
    private val qr: RB,
    private val mapper: CountryMapper,
    private val modeMapper: ModeMapper,
    private val logger: KLogger = KotlinLogging.logger {},
) :
    CountryRepository {

    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        val coroutineLimit = Semaphore(permits = 1)
        scope.launch {
            val query = qr.l("mutation.create.edit.country")
            val countries = loadCountriesFromClassPath()

            val deferredCountries = countries.map {
                async {
                    dbClient.exec(query = query).bind("input", jsonOf(it)).fetch().awaitRowsUpdated()
                    it
                }
            }.chunked(15)

            for (chunkedCountries in deferredCountries) {
                coroutineLimit.withPermit {
                    chunkedCountries.awaitAll()
                }
            }
        }
    }

    override suspend fun loadCountriesFromClassPath(): Iterable<CountryDto> {
        val countriesEngFile = ClassPathResource("countries/countries-en.json")
        val countriesFrFile = ClassPathResource("countries/countries-fr.json")
        if (countriesEngFile.exists()) {
            val enStringCountries = IOUtils.toString(countriesEngFile.inputStream, StandardCharsets.UTF_8.name())
            val enJsonCountries = JSONArray(enStringCountries)
            var frJsonCountries: JSONObject? = null
            if (countriesFrFile.exists()) {
                val frStringCountries = IOUtils.toString(countriesFrFile.inputStream, StandardCharsets.UTF_8.name())
                frJsonCountries = JSONObject(frStringCountries)
            }
            val additionalLanguages = if (frJsonCountries != null) arrayOf(frJsonCountries) else emptyArray()
            return mapper.getListDto(enMap = enJsonCountries, otherLangMap = additionalLanguages)
        }
        // TODO: throw RuntimeException
        return emptyList()
    }

    override suspend fun country(id: UUID, token: UUID): Optional<Country> {
        val query = qr.l("query.find.country")
        return dbClient.exec(query = query)
            .bind("id", id)
            .bind("token", token)
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun countries(first: Int, after: UUID?, token: UUID): Iterable<Country> {
        val query = qr.l("query.retrieve.country")
        return dbClient.exec(query = query)
            .bind("first", first)
            .bind("after", parameterOrNull(after))
            .bind("token", token)
            .map(mapper::list)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyList() }
    }

    override suspend fun modes(countries: List<Country>, token: UUID?): Map<Country, Iterable<Mode>> {
        val query = qr.l("query.retrieve.modes.country")
        return Flux.fromIterable(countries)
            .parallel()
            .flatMap { country ->
                dbClient.exec(query)
                    .bind("id", parameterOrNull(country.id))
                    .bind("token", parameterOrNull(token))
                    .map(modeMapper::list)
                    .all()
                    .map { AbstractMap.SimpleEntry(country, it) }
                    .onErrorResume { throw it }
            }
            .runOn(Schedulers.parallel())
            .sequential()
            .collectList()
            .map { entries -> entries.associateBy({ it.key }, { it.value ?: emptyList() }) }
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyMap() }
    }

}