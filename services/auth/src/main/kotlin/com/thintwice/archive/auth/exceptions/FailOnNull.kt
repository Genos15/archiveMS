package com.thintwice.archive.auth.exceptions

fun failOnNull(message: String = "something went wrong from server. try again later"): Nothing =
    throw AssertionError(message)