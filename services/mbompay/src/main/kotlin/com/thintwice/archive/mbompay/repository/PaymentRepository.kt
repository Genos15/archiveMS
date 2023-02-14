package com.thintwice.archive.mbompay.repository

import com.thintwice.archive.mbompay.domain.input.PaymentIntentInput
import com.thintwice.archive.mbompay.domain.model.JCard
import com.thintwice.archive.mbompay.domain.model.JTransaction
import java.time.OffsetDateTime
import java.util.*

interface PaymentRepository {
    suspend fun update(input: PaymentIntentInput, accessToken: String? = null): Optional<JTransaction>
    suspend fun list(first: Long, after: UUID? = null, accessToken: String): Iterable<JTransaction>
    suspend fun list(fromDate: OffsetDateTime, toDate: OffsetDateTime, accessToken: String): Iterable<JTransaction>

    suspend fun card(transactions: List<JTransaction>): Map<JTransaction, JCard>
}