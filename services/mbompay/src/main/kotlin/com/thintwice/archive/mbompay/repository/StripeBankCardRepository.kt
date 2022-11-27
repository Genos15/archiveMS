package com.thintwice.archive.mbompay.repository

import com.thintwice.archive.mbompay.domain.input.BankCardInput

interface StripeBankCardRepository : OwnerFactoryRepository<BankCardInput, BankCardInput>