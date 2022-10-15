package com.thintwice.archive.user.exceptions

fun failOnNull(message: String = "something went wrong from server. try again later"): Nothing =
    throw AssertionError(message)