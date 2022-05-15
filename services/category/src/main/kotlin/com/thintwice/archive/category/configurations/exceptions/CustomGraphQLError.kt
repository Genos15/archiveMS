package com.thintwice.archive.category.configurations.exceptions

class CustomGraphQLError(message: String) : RuntimeException("[p:001]".plus(message))