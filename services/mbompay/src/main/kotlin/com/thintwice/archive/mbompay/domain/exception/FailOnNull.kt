package com.thintwice.archive.mbompay.domain.exception

fun failOnNull(message: String = "something went wrong from server. try again later"): Nothing =
    throw AssertionError(message)