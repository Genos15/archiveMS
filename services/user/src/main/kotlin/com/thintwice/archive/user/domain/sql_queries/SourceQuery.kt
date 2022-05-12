package com.thintwice.archive.user.domain.sql_queries


object SourceQuery {

    const val edit: String = "select users_on_edit(:input::JSONB, :token)"

    const val delete: String = "select users_on_delete(:id, :token)"

    const val find: String = "select users_on_find(:id, :token)"

}