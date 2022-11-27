package com.thintwice.archive.mbompay.domain.model

data class UserCredential(private val username: String, private val userRole: String) : Credential {
    override fun getUsername(): String = username

    override fun getUserRole(): String = userRole
}