package com.thintwice.archive.mbompay.service

import com.stripe.model.Customer
import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.configuration.database.DbClient
import com.thintwice.archive.mbompay.configuration.security.JWTService
import com.thintwice.archive.mbompay.domain.common.jsonOf
import com.thintwice.archive.mbompay.domain.common.parameterOrNull
import com.thintwice.archive.mbompay.domain.input.CustomerInput
import com.thintwice.archive.mbompay.domain.input.JCustomerInput
import com.thintwice.archive.mbompay.domain.mapper.CustomerMapper
import com.thintwice.archive.mbompay.domain.mapper.JCardMapper
import com.thintwice.archive.mbompay.domain.mapper.JwtTokenMapper
import com.thintwice.archive.mbompay.domain.model.JCard
import com.thintwice.archive.mbompay.domain.model.JCustomer
import com.thintwice.archive.mbompay.domain.model.JwtToken
import com.thintwice.archive.mbompay.repository.CustomerRepository
import kotlinx.coroutines.reactive.awaitFirstOrDefault
import kotlinx.coroutines.reactive.awaitFirstOrElse
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
    private val jwtService: JWTService,
    private val cardMapper: JCardMapper,
) : CustomerRepository {

    override suspend fun customer(input: CustomerInput): Optional<JCustomer> {
        val query = qr.l("mutation.create.edit.customer")
        return dbClient.exec(query = query)
            .bind("input", jsonOf(input))
            .map(mapper::factory)
            .first()
            .awaitFirstOrDefault(Optional.empty())
    }

    override suspend fun customer(id: UUID): Optional<JCustomer> {
        val query = qr.l("query.find.customer")
        val token: String? = null
        return dbClient.exec(query = query)
            .bind("id", id)
            .bind("token", parameterOrNull(token))
            .map(mapper::factory)
            .first()
            .awaitFirstOrDefault(Optional.empty())
    }

    override suspend fun customer(accessToken: String): Optional<JCustomer> {
        val query = qr.l("query.find.customer")
        val id: UUID? = null
        return dbClient.exec(query = query)
            .bind("id", parameterOrNull(id))
            .bind("token", accessToken)
            .map(mapper::factory)
            .first()
            .awaitFirstOrDefault(Optional.empty())
    }

    override suspend fun customers(first: Int, after: UUID?): Iterable<JCustomer> {
        val query = qr.l("query.retrieve.customer")
        return dbClient.exec(query = query)
            .bind("first", first)
            .bind("after", Parameter.fromOrEmpty(after, UUID::class.java))
            .map(mapper::list)
            .first()
            .awaitFirstOrDefault(emptyList())
    }

    override suspend fun jwtToken(customers: List<JCustomer>): Map<JCustomer, JwtToken> {
        val query = qr.l("mutation.jwt.token.create")
        return Flux.fromIterable(customers)
            .filter { it.email != null && it.emailVerified == true }
            .parallel()
            .flatMap { customer ->
                val jwtToken = jwtService.generateJwtToken(
                    username = customer.email!!,
                    roles = arrayOf(kFIREBASE_DEFAULT_USER_ROLE)
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
            .awaitFirstOrElse { emptyMap() }
    }

    override suspend fun defaultCard(customers: List<JCustomer>): Map<JCustomer, JCard> {
        val query = qr.l("query.retrieve.customer.default.card")
        return Flux.fromIterable(customers)
            .filter { it.email != null && it.emailVerified == true }
            .parallel()
            .flatMap { customer ->
                dbClient.exec(query = query)
                    .bind("id", customer.id)
                    .map(cardMapper::factory)
                    .first()
                    .map { AbstractMap.SimpleEntry(customer, it.orElseGet { null }) }
                    .filter { it.value != null }
            }
            .runOn(Schedulers.parallel())
            .sequential()
            .collectList()
            .map { entries -> entries.associateBy({ it.key }, { it.value }) }
            .awaitFirstOrElse { emptyMap() }
    }

    override suspend fun updateStripeId(uid: String, accessToken: String) {
        dbClient.exec(
            query = """
            UPDATE customers SET customers.uid = :uid 
            WHERE customers.id = user_id_by_access_token (
                   access_token := :token,
                   is_required := false
            )
        """.trimIndent()
        )
            .bind("uid", uid)
            .bind("token", accessToken)
            .map(mapper::list)
            .first()
            .awaitFirstOrDefault(emptyList())
    }

    override suspend fun stripeEventUpdate(customer: Customer) {
        val input = JCustomerInput(customer = customer)
        println(input)
        val query = qr.l("mutation.stripe.update.customer")
        dbClient.exec(query = query)
            .bind("input", jsonOf(input))
            .fetch()
            .first()
            .map { it.values.firstOrNull() as Boolean? ?: false }
            .awaitFirstOrDefault(false)
    }

    companion object {
        const val kFIREBASE_DEFAULT_USER_ROLE = "ROLE_FIREBASE"
    }
}