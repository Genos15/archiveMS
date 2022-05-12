package com.thintwice.archive.user.resolvers

import com.thintwice.archive.user.configurations.token.TokenAnalyzer
import com.thintwice.archive.user.domain.model.User
import com.thintwice.archive.user.services.UserService
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserQuery(private val service: UserService, private val deduct: TokenAnalyzer) : GraphQLQueryResolver {
    suspend fun user(id: UUID? = null, environment: DataFetchingEnvironment): Optional<User> =
        service.user(id = id, token = deduct(environment = environment))
}