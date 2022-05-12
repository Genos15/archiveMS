package com.thintwice.archive.auth.configurations.exceptions

class CustomGraphQLError(message: String) : RuntimeException("[p:001]".plus(message))