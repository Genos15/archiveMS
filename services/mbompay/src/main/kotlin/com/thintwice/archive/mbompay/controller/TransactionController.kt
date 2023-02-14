package com.thintwice.archive.mbompay.controller

import com.stripe.exception.StripeException
import com.thintwice.archive.mbompay.domain.model.JCard
import com.thintwice.archive.mbompay.domain.model.JTransaction
import com.thintwice.archive.mbompay.repository.PaymentRepository
import org.springframework.graphql.data.method.annotation.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import java.time.OffsetDateTime
import java.util.*


@Controller
class TransactionController(private val service: PaymentRepository) {

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @QueryMapping(name = "transactions")
    suspend fun transactions(
        @Argument first: Long,
        @Argument after: UUID?,
        authentication: Authentication?,
    ): Iterable<JTransaction> {

        val result = runCatching {
            if (authentication?.name == null) {
                throw RuntimeException("invalid token")
            }
            return service.list(first = first, accessToken = authentication.name, after = after)
        }.recover {
            when (it) {
                is StripeException -> {
                    println("StripeException Exception $it")
                }

                else -> println("Unknown Exception $it")
            }
            emptyList<JTransaction>()
        }

        return result.getOrNull()!!
    }


    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @QueryMapping(name = "transactionsByDate")
    suspend fun transactionsByDate(
        @Argument fromDate: OffsetDateTime,
        @Argument toDate: OffsetDateTime,
        authentication: Authentication?,
    ): Iterable<JTransaction> {

        val result = runCatching {
            if (authentication?.name == null) {
                throw RuntimeException("invalid token")
            }
            return service.list(fromDate = fromDate, accessToken = authentication.name, toDate = toDate)
        }.recover {
            when (it) {
                is StripeException -> {
                    println("StripeException Exception $it")
                }

                else -> println("Unknown Exception $it")
            }
            emptyList<JTransaction>()
        }

        return result.getOrNull()!!
    }

    @PreAuthorize("hasAnyRole('FIREBASE', 'CUSTOMER')")
    @BatchMapping(field = "card", typeName = "Transaction")
    suspend fun jwtToken(transactions: List<JTransaction>): Map<JTransaction, JCard> {
        return service.card(transactions = transactions)
    }
}