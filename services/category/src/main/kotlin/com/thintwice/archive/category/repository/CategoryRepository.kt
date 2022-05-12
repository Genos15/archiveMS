package com.thintwice.archive.category.repository

import com.thintwice.archive.category.domain.input.CategoryInput
import com.thintwice.archive.category.domain.model.Category
import java.util.*

interface CategoryRepository {
    suspend fun create(input: CategoryInput): Optional<Category>
    suspend fun delete(id: UUID): Boolean
    suspend fun find(id: UUID): Optional<Category>
    suspend fun retrieve(first: Int, after: UUID? = null): List<Category>
    suspend fun search(text: String, first: Int, after: UUID? = null): List<Category>
    suspend fun count(): Long
}