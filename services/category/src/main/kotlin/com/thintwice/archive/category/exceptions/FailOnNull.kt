package com.thintwice.archive.category.exceptions

fun failOnNull(message: String = "something went wrong from server. try again later"): Nothing =
    throw AssertionError(message)