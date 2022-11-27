package com.thintwice.archive.mbompay.domain.exception

class InternalException(source: String?) : RuntimeException(source?.split("]")?.last()?.trim { it <= ' ' })