package com.thintwice.archive.mbompay.configuration.payment

import com.braintreegateway.BraintreeGateway
import com.braintreegateway.Environment
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component


@Component
class BraintreeConfiguration {

    @Bean
    fun braintreeGateway(): BraintreeGateway {
        return BraintreeGateway(
            Environment.SANDBOX,
            "your_merchant_id",
            "your_public_key",
            "your_private_key"
        )
    }

}