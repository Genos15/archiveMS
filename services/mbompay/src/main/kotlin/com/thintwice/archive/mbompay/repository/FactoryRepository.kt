package com.thintwice.archive.mbompay.repository


import com.thintwice.archive.mbompay.domain.common.JsonEquivalent
import java.util.*

interface FactoryRepository<I : JsonEquivalent, T> {
   fun create(input: I): T
   fun update(input: I): T
   fun delete(input: I): Boolean
   fun find(input: I): Optional<T>
   fun retrieve(first: Int): Iterable<T>
   fun search(options: Map<String, Any>): Iterable<T>
}