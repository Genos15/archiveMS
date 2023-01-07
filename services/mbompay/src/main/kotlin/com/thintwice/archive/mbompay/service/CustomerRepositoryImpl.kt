package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.configuration.database.DbClient
import com.thintwice.archive.mbompay.configuration.security.JWTService
import com.thintwice.archive.mbompay.domain.common.jsonOf
import com.thintwice.archive.mbompay.domain.input.CustomerInput
import com.thintwice.archive.mbompay.domain.mapper.CustomerMapper
import com.thintwice.archive.mbompay.domain.mapper.JwtTokenMapper
import com.thintwice.archive.mbompay.domain.model.Customer
import com.thintwice.archive.mbompay.domain.model.JwtToken
import com.thintwice.archive.mbompay.repository.CustomerRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.r2dbc.core.Parameter
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.util.*

@Service
class CustomerRepositoryImpl(
    private val qr: RB,
    private val dbClient: DbClient,
    private val mapper: CustomerMapper,
    private val jwtMapper: JwtTokenMapper,
    private val logger: KLogger = KotlinLogging.logger {},
    private val jwtService: JWTService,
) : CustomerRepository {

    override suspend fun customer(input: CustomerInput): Optional<Customer> {
        val query = qr.l("mutation.create.edit.customer")
        return dbClient.exec(query = query)
            .bind("input", jsonOf(input))
            .map(mapper::factory)
            .first()
            .doOnError { println("error = ${it.message}") }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun customer(id: UUID): Optional<Customer> {
        val query = qr.l("query.find.customer")
        return dbClient.exec(query = query)
            .bind("id", id)
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun customer(accessToken: String): Optional<Customer> {
        val query = qr.l("query.find.customer")
        return dbClient.exec(query = query)
            .bind("token", accessToken)
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun customers(first: Int, after: UUID?): Iterable<Customer> {
        val query = qr.l("query.retrieve.customer")
        return dbClient.exec(query = query)
            .bind("first", first)
            .bind("after", Parameter.fromOrEmpty(after, UUID::class.java))
            .map(mapper::list)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyList() }
    }

    override suspend fun jwtToken(customers: List<Customer>): Map<Customer, JwtToken> {
        val query = qr.l("mutation.jwt.token.create")
        return Flux.fromIterable(customers)
            .filter { it.email != null && it.emailVerified == true }
            .parallel()
            .flatMap { customer ->
                val jwtToken = jwtService.generateJwtToken(
                    username = customer.email!!,
                    roles = arrayOf("ROLE_FIREBASE")
                )
                dbClient.exec(query = query)
                    .bind("input", jsonOf(jwtToken))
                    .bind("username", customer.email)
                    .map(jwtMapper::factory)
                    .first()
                    .map { AbstractMap.SimpleEntry(customer, it.orElseGet { null }) }
                    .filter { it.value != null }
            }
            .runOn(Schedulers.parallel())
            .sequential()
            .collectList()
            .map { entries -> entries.associateBy({ it.key }, { it.value }) }
            .doOnError { println { it.message } }
            .log()
            .awaitFirstOrElse { emptyMap() }
    }
}