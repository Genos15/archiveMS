package com.thintwice.archive.mbompay.domain.model

import java.util.*

data class Admin(val id: UUID, val name: String, val email: String, val phone: String)
    : User(id = id, name = name, email = email, phone = phone)