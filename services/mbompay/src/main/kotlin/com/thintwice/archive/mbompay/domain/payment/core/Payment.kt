package com.thintwice.archive.mbompay.domain.payment.core

interface Payment <T> {
    suspend fun process()

    suspend fun capture()

    suspend fun authorize()

    suspend fun reversal()

    suspend fun refund()

    suspend fun cancel()

    suspend fun preAuthorization(customerId: String) : T
}