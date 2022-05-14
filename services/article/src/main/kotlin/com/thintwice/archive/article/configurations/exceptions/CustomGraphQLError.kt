package com.thintwice.archive.article.configurations.exceptions

class CustomGraphQLError(message: String) : RuntimeException("[p:001]".plus(message))