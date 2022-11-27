package com.thintwice.archive.mbompay.domain.dto

import com.thintwice.archive.mbompay.domain.model.Operator
import java.util.*

data class OperatorDto(val id: UUID, val name: String, val email: String, val phone: String) {
    val operator: Operator
        get() = Operator(id = id, name = name, email = email, phone = phone)
}