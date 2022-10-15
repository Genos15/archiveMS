package com.thintwice.archive.mbompay.domain.common

import java.util.*

interface MapperState<T, R, P> {
    fun list(row: T, o: R): List<P>
    fun factory(row: T, o: R): Optional<P>
}