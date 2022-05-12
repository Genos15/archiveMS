package com.thintwice.archive.auth.resolvers

import com.thintwice.archive.auth.domain.model.Token
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenQuery : GraphQLQueryResolver {
    suspend fun token(): Optional<Token> = Optional.empty()
}