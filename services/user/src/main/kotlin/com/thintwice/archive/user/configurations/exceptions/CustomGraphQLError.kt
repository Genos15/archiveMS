package com.thintwice.archive.user.configurations.exceptions

class CustomGraphQLError(message: String) : RuntimeException("[p:001]".plus(message))