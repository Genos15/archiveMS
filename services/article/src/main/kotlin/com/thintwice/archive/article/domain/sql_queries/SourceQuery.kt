package com.thintwice.archive.article.domain.sql_queries


object SourceQuery {

    const val delete = "select articles_on_delete(:id, :token)"

    const val find = "select articles_on_find(:id, :token)"

    const val sell = "select articles_on_sell(:id, :token)"

    const val wishlist = "select users_articles_on_retrieve_wishlist(:first, :after, :token)"

    const val removeInWishlist = "select users_articles_on_remove_in_wishlist(:id, :token)"

    const val addInWishlist = "select users_articles_on_add_in_wishlist(:id, :token)"

    const val categoryArticles = "select category_articles_on_retrieve(:id, :first, :after, :token)"

    const val userArticles = "select users_articles_on_retrieve(:id, :first, :after, :token)"

    const val myArticles = "select users_my_articles_on_retrieve(:first, :after, :token)"

    const val search = "select articles_on_search(:text, :first, :after, :token)"

    const val retrieve = "select articles_on_retrieve(:first, :after, :token)"

    const val create = "select articles_on_create_or_edit(:input::JSONB, :token)"

}