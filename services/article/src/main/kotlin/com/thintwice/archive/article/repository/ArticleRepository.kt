package com.thintwice.archive.article.repository

import com.thintwice.archive.article.domain.input.ArticleInput
import com.thintwice.archive.article.domain.model.Article
import java.util.*

interface ArticleRepository {
    suspend fun create(input: ArticleInput, token: UUID): Optional<Article>
    suspend fun delete(id: UUID, token: UUID): Boolean
    suspend fun article(id: UUID, token: UUID): Optional<Article>
    suspend fun sell(id: UUID, token: UUID): Optional<Article>
    suspend fun wishlist(first: Int, after: UUID? = null, token: UUID): List<Article>
    suspend fun removeInWishlist(id: UUID, token: UUID): Boolean
    suspend fun addInWishlist(id: UUID, token: UUID): Optional<Article>
    suspend fun categoryArticles(id: UUID, first: Int, after: UUID? = null, token: UUID): List<Article>
    suspend fun userArticles(id: UUID, first: Int, after: UUID? = null, token: UUID): List<Article>
    suspend fun myArticles(first: Int, after: UUID? = null, token: UUID): List<Article>
    suspend fun search(text: String, first: Int, after: UUID? = null, token: UUID): List<Article>
    suspend fun retrieve(first: Int, after: UUID? = null, token: UUID): List<Article>
}