package com.thintwice.archive.mbompay.repository


import com.thintwice.archive.mbompay.domain.common.JsonEquivalent
import java.util.*

interface OwnerFactoryRepository<I : JsonEquivalent, T> {
   fun create(input: I, customerId: String): T
   fun update(input: I, customerId: String): T
   fun delete(input: I, customerId: String): Boolean
   fun find(input: I, customerId: String): Optional<T>
   fun retrieve(first: Int, customerId: String): Iterable<T>
   fun search(options: Map<String, Any>, customerId: String): Iterable<T>
}