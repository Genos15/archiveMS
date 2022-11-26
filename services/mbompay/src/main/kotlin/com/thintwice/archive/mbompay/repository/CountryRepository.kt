package com.thintwice.archive.mbompay.repository


import com.thintwice.archive.mbompay.domain.dto.CountryDto
import com.thintwice.archive.mbompay.domain.model.Country
import com.thintwice.archive.mbompay.domain.model.Mode
import java.util.*

interface CountryRepository {
    suspend fun loadCountriesFromClassPath(): Iterable<CountryDto>
    suspend fun country(id: UUID, token: UUID): Optional<Country>
    suspend fun countries(first: Int, after: UUID? = null, token: UUID): Iterable<Country>
    suspend fun modes(countries: List<Country>, token: UUID? = null): Map<Country, Iterable<Mode>>

}