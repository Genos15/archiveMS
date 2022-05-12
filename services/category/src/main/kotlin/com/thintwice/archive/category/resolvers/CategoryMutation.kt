package com.thintwice.archive.category.resolvers

import com.thintwice.archive.category.domain.input.CategoryInput
import com.thintwice.archive.category.domain.model.Category
import com.thintwice.archive.category.services.CategoryService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import java.util.*

@Component
class CategoryMutation(private val service: CategoryService) : GraphQLMutationResolver {

    suspend fun createOrEdit(input: CategoryInput): Optional<Category> = service.create(input = input)

    suspend fun delete(id: UUID): Boolean = service.delete(id = id)

}