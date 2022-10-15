package com.thintwice.archive.user.resolvers

import com.thintwice.archive.user.configurations.token.TokenAnalyzer
import com.thintwice.archive.user.domain.input.UserInput
import com.thintwice.archive.user.domain.model.User
import com.thintwice.archive.user.services.UserService
import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserMutation(private val service: UserService, private val deduct: TokenAnalyzer) : GraphQLMutationResolver {

    suspend fun edit(input: UserInput, environment: DataFetchingEnvironment): Optional<User> =
        service.edit(input = input, token = deduct(environment = environment))

    suspend fun delete(id: UUID, environment: DataFetchingEnvironment): Boolean =
        service.delete(id = id, token = deduct(environment = environment))

}