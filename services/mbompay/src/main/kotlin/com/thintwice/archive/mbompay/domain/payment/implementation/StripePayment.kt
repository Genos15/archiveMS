package com.thintwice.archive.mbompay.domain.payment.implementation

import com.thintwice.archive.mbompay.domain.payment.core.Payment

class StripePayment: Payment<Any> {
    override suspend fun process() {
        TODO("Not yet implemented")
    }

    override suspend fun capture() {
        TODO("Not yet implemented")
    }

    override suspend fun authorize() {
        TODO("Not yet implemented")
    }

    override suspend fun reversal() {
        TODO("Not yet implemented")
    }

    override suspend fun refund() {
        TODO("Not yet implemented")
    }

    override suspend fun cancel() {
        TODO("Not yet implemented")
    }

    override suspend fun preAuthorization(customerId: String): Any {
        TODO("Not yet implemented")
    }

}