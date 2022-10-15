package com.thintwice.archive.article.resolvers

import com.thintwice.archive.article.configurations.token.TokenAnalyzer
import com.thintwice.archive.article.domain.input.ArticleInput
import com.thintwice.archive.article.domain.model.Article
import com.thintwice.archive.article.repository.ArticleRepository
import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.util.*

@Component
class ArticleMutation(private val service: ArticleRepository, private val deduct: TokenAnalyzer) :
    GraphQLMutationResolver {

    suspend fun createOrEdit(input: ArticleInput, environment: DataFetchingEnvironment): Optional<Article> =
        service.create(input = input, token = deduct(environment = environment))

    suspend fun delete(id: UUID, environment: DataFetchingEnvironment): Boolean =
        service.delete(id = id, token = deduct(environment = environment))

    suspend fun removeInWishlist(id: UUID, environment: DataFetchingEnvironment): Boolean =
        service.removeInWishlist(id = id, token = deduct(environment = environment))

    suspend fun addInWishlist(id: UUID, environment: DataFetchingEnvironment): Optional<Article> =
        service.addInWishlist(id = id, token = deduct(environment = environment))

    suspend fun sell(id: UUID, environment: DataFetchingEnvironment): Optional<Article> =
        service.sell(id = id, token = deduct(environment = environment))

}