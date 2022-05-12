package com.thintwice.archive.category.domain.sql_queries


object SourceQuery {
    const val retrieve: String = "select categories_article_on_retrieve(:first, :after)"

    const val create: String = "select categories_article_on_create_or_edit(:input::JSONB)"

    const val edit: String = "select categories_article_on_create_or_edit(:input)"

    const val delete: String = "select categories_article_on_delete(:id)"

    const val count: String = "select categories_article_on_count()"

    const val search: String = "select categories_article_on_search(:text, :first, :after)"

    const val find: String = "select categories_article_on_find(:id)"
}