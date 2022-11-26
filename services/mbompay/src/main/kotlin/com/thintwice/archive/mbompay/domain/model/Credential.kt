package com.thintwice.archive.mbompay.domain.model

interface Credential {
    fun getUsername(): String

    fun getUserRole(): String
}