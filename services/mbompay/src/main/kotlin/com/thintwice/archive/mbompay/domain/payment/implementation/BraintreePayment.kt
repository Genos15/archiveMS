package com.thintwice.archive.mbompay.domain.payment.implementation

import com.braintreegateway.BraintreeGateway
import com.braintreegateway.ClientTokenRequest
import com.thintwice.archive.mbompay.domain.payment.core.Payment


class BraintreePayment(private val gateway: BraintreeGateway) : Payment<String> {
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

    override suspend fun preAuthorization(customerId: String): String {
        val clientTokenRequest = ClientTokenRequest().customerId(customerId)
        return gateway.clientToken().generate(clientTokenRequest)
    }

}