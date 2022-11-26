package com.thintwice.archive.mbompay.configuration.database

import org.springframework.r2dbc.core.DatabaseClient

interface DbClient {
    fun exec(query: String): DatabaseClient.GenericExecuteSpec
}