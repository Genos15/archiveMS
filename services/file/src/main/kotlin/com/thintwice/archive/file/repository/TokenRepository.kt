package com.thintwice.archive.file.repository

import java.util.*

interface TokenRepository {
    suspend fun isActive(token: UUID): Boolean
}