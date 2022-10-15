package com.thintwice.archive.user.configurations.object_mapping

import java.util.*

interface MapperState<T, R, P> {
    fun asList(row: T, o: R): List<P>
    fun asSingle(row: T, o: R): Optional<P>
}