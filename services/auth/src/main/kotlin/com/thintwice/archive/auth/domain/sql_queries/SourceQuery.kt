package com.thintwice.archive.auth.domain.sql_queries


object SourceQuery {

    const val create: String = "select users_on_create_or_login(:input::JSONB)"

    const val refresh: String = "select users_on_refresh_token(:input)"
}