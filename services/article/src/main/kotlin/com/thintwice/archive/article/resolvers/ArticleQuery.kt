package com.thintwice.archive.article.resolvers

import com.thintwice.archive.article.configurations.cursor.GenericRetrieveConnection
import com.thintwice.archive.article.configurations.token.TokenAnalyzer
import com.thintwice.archive.article.domain.model.Article
import com.thintwice.archive.article.repository.ArticleRepository
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.relay.Connection
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.util.*

@Component
class ArticleQuery(private val service: ArticleRepository, private val deduct: TokenAnalyzer) : GraphQLQueryResolver,
    GenericRetrieveConnection {

    suspend fun article(id: UUID, environment: DataFetchingEnvironment): Optional<Article> =
        service.article(id = id, token = deduct(environment = environment))

    suspend fun articles(first: Int, after: UUID? = null, environment: DataFetchingEnvironment): Connection<Article> =
        perform(
            entries = service.retrieve(first = first, after = after, token = deduct(environment = environment)),
            first = first,
            after = after
        )

    suspend fun search(
        text: String,
        first: Int,
        after: UUID? = null,
        environment: DataFetchingEnvironment,
    ): Connection<Article> =
        perform(
            entries = service.search(text = text,
                first = first,
                after = after,
                token = deduct(environment = environment)),
            first = first,
            after = after
        )

    suspend fun categoryArticles(
        id: UUID,
        first: Int,
        after: UUID? = null,
        environment: DataFetchingEnvironment,
    ): Connection<Article> =
        perform(
            entries = service.categoryArticles(id = id,
                first = first,
                after = after,
                token = deduct(environment = environment)),
            first = first,
            after = after
        )

    suspend fun userArticles(
        id: UUID,
        first: Int,
        after: UUID? = null,
        environment: DataFetchingEnvironment,
    ): Connection<Article> =
        perform(
            entries = service.userArticles(id = id,
                first = first,
                after = after,
                token = deduct(environment = environment)),
            first = first,
            after = after
        )

    suspend fun myArticles(
        first: Int,
        after: UUID? = null,
        environment: DataFetchingEnvironment,
    ): Connection<Article> =
        perform(
            entries = service.myArticles(
                first = first,
                after = after,
                token = deduct(environment = environment)),
            first = first,
            after = after
        )

    suspend fun myWishlist(
        first: Int,
        after: UUID? = null,
        environment: DataFetchingEnvironment,
    ): Connection<Article> =
        perform(
            entries = service.wishlist(
                first = first,
                after = after,
                token = deduct(environment = environment)),
            first = first,
            after = after
        )
}