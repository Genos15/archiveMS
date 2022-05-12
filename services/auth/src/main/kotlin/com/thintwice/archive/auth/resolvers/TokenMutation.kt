package com.thintwice.archive.auth.resolvers

import com.thintwice.archive.auth.configurations.token.TokenAnalyzer
import com.thintwice.archive.auth.domain.input.TokenInput
import com.thintwice.archive.auth.domain.model.Token
import com.thintwice.archive.auth.services.CategoryService
import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenMutation(private val service: CategoryService, private val deduct: TokenAnalyzer) : GraphQLMutationResolver {

    suspend fun login(input: TokenInput): Optional<Token> = service.login(input = input)

    suspend fun refresh(environment: DataFetchingEnvironment): Optional<Token> =
        service.refresh(input = deduct(environment = environment))

}