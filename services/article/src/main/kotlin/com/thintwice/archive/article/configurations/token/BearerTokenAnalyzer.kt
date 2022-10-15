package com.thintwice.archive.article.configurations.token

import com.thintwice.archive.article.configurations.token.TokenAnalyzer
import com.thintwice.archive.article.configurations.exceptions.CustomGraphQLError
import com.thintwice.archive.article.configurations.context.CustomGraphQLContext
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.util.*
import kotlin.jvm.Throws

@Component
class BearerTokenAnalyzer : TokenAnalyzer {

    @Throws(CustomGraphQLError::class)
    override fun invoke(environment: DataFetchingEnvironment): UUID {
        val context: CustomGraphQLContext = environment.getContext()
        val request = context.httpServletRequest
        val full = request.getHeader("Authorization") ?: ""
        if (!full.lowercase().contains("bearer")) {
            throw CustomGraphQLError("please add a token")
        }
        val token = full.lowercase().replace("bearer ", "", ignoreCase = true)
        return try {
            UUID.fromString(token.trim())
        } catch (e: Exception) {
            throw CustomGraphQLError("token not found")
        }
    }

}