package com.thintwice.archive.mbompay.configuration.database

import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import org.springframework.util.Assert

@Component
class DbClientImpl(private val database: DatabaseClient) : DbClient {
    override fun exec(query: String): DatabaseClient.GenericExecuteSpec {
        Assert.hasText(query, "SQL must not be null or empty")
        return database.sql { query }
    }
}