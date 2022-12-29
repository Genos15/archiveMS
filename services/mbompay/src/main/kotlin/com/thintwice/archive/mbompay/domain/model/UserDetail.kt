package com.thintwice.archive.mbompay.domain.model
data class UserDetail(
    val displayName: String? = null,
    val email: String? = null,
    val emailVerified: Boolean? = false,
    val isAnonymous: Boolean? = true,
    val phoneNumber: String? = null,
    val photoURL: String? = null,
    val providerId: String? = null,
    val providerUid: String? = null,
    val firebaseUid: String? = null,
)