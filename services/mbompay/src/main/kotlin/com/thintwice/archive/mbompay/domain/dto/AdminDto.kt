package com.thintwice.archive.mbompay.domain.dto

import com.thintwice.archive.mbompay.domain.model.Admin
import java.util.*

data class AdminDto(val id: UUID, val name: String, val email: String, val phone: String) {
    val admin: Admin
        get() = Admin(id = id, name = name, email = email, phone = phone)
}