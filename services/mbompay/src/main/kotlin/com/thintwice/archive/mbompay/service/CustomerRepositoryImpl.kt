package com.thintwice.archive.mbompay.service

import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.configuration.database.DbClient
import com.thintwice.archive.mbompay.domain.common.jsonOf
import com.thintwice.archive.mbompay.domain.input.CustomerInput
import com.thintwice.archive.mbompay.domain.mapper.CustomerMapper
import com.thintwice.archive.mbompay.domain.model.Customer
import com.thintwice.archive.mbompay.repository.CustomerRepository
import com.thintwice.archive.mbompay.repository.StripeCustomerRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.r2dbc.core.Parameter
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerRepositoryImpl(
    private val qr: RB,
    private val dbClient: DbClient,
    private val mapper: CustomerMapper,
    private val logger: KLogger = KotlinLogging.logger {},
    private val stripeService: StripeCustomerRepository,
) : CustomerRepository {

    override suspend fun customer(input: CustomerInput): Optional<Customer> {
//        val newInput = if (input.id == null) stripeService.create(input = input) else input
        val query = qr.l("mutation.create.edit.customer")
//        println(newInput)
        return dbClient.exec(query = query)
            .bind("input", jsonOf(input))
            .map(mapper::factory)
            .first()
            .doOnError { logger.error { it.message } }
//            .map {
//                if (input.id != null) {
//                    input.stripeCustomerId = it?.stripeCustomerId
//                    stripeService.update(input = input)
//                }
//                Optional.ofNullable(it?.customer)
//            }
            .log()
            .awaitFirstOrElse { Optional.empty() }
    }

    override suspend fun customer(id: UUID): Optional<Customer> {
        val query = qr.l("query.find.customer")
        return dbClient.exec(query = query)
            .bind("id", id)
//            .bind("token", token)
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
//            .bind("token", token)
            .map(mapper::list)
            .first()
            .doOnError { logger.error { it.message } }
            .log()
            .awaitFirstOrElse { emptyList() }
    }
}