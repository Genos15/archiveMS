package com.thintwice.archive.mbompay.repository


import com.thintwice.archive.mbompay.domain.input.OperatorInput
import com.thintwice.archive.mbompay.domain.model.Operator
import java.util.*

interface OperatorRepository {
    suspend fun operator(input: OperatorInput, token: UUID): Optional<Operator>
    suspend fun operator(id: UUID, token: UUID): Optional<Operator>
    suspend fun operators(first: Int, after: UUID? = null, token: UUID): Iterable<Operator>
}