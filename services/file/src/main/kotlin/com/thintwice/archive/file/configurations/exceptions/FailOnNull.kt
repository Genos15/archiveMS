package com.thintwice.archive.file.configurations.exceptions

fun failOnNull(message: String = "something went wrong from server. try again later"): Nothing =
    throw AssertionError(message)