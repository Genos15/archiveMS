package com.thintwice.archive.category.resolvers

import com.thintwice.archive.category.configurations.cursor.GenericRetrieveConnection
import com.thintwice.archive.category.domain.model.Category
import com.thintwice.archive.category.services.CategoryService
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.relay.Connection
import org.springframework.stereotype.Component
import java.util.*

@Component
class CategoryQuery(val service: CategoryService) : GraphQLQueryResolver, GenericRetrieveConnection {

    suspend fun categories(first: Int, after: UUID? = null): Connection<Category> = perform(
        entries = service.categories(first = first, after = after),
        first = first,
        after = after
    )

    suspend fun search(
        text: String,
        first: Int,
        after: UUID? = null,
    ): Connection<Category> = perform(
        entries = service.search(text = text, first = first, after = after),
        first = first,
        after = after
    )

    suspend fun category(id: UUID): Optional<Category> = service.category(id = id)

    suspend fun count(): Long = service.count()
}