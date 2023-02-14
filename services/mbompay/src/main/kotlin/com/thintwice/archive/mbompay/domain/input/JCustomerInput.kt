package com.thintwice.archive.mbompay.domain.input

import com.stripe.model.Customer
import com.thintwice.archive.mbompay.domain.common.JsonEquivalent

data class JCustomerInput(
    val uid: String,
    val email: String,
    val defaultSource: String,
) : JsonEquivalent {
    constructor(customer: Customer) : this(
        uid = customer.id,
        email = customer.email,
        defaultSource = customer.defaultSource,
    )
}

