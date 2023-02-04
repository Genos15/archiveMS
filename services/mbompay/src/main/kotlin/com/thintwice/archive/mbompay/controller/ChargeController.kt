package com.thintwice.archive.mbompay.controller

import com.stripe.model.Charge
import com.thintwice.archive.mbompay.repository.StripeChargeRepository
import org.springframework.graphql.data.method.annotation.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class ChargeController(private val service: StripeChargeRepository) {
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @MutationMapping(name = "send")
    suspend fun send(@Argument amount: Long, authentication: Authentication?): Charge? {
        if (authentication?.name == null) {
            throw RuntimeException("invalid token")
        }
        return service.send(amount = amount, token = authentication.name)
    }
}