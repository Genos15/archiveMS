package com.thintwice.archive.category.services

import com.thintwice.archive.category.domain.input.CategoryInput
import com.thintwice.archive.category.domain.model.Category
import com.thintwice.archive.category.repository.CategoryRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CategoryService(private val it: CategoryRepository) {
    suspend fun categories(first: Int, after: UUID? = null): List<Category> = it.retrieve(first = first, after = after)
    suspend fun search(text: String, first: Int, after: UUID? = null): List<Category> =
        it.search(text = text, first = first, after = after)

    suspend fun category(id: UUID): Optional<Category> = it.find(id = id)
    suspend fun create(input: CategoryInput): Optional<Category> = it.create(input = input)
    suspend fun count(): Long = it.count()
    suspend fun delete(id: UUID): Boolean = it.delete(id = id)
}