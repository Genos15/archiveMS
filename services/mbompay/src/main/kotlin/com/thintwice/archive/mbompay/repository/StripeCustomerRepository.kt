package com.thintwice.archive.mbompay.repository

import com.thintwice.archive.mbompay.domain.input.CustomerInput

interface StripeCustomerRepository : FactoryRepository<CustomerInput, CustomerInput>