package com.thintwice.archive.mbompay.domain.input

import com.thintwice.archive.mbompay.domain.common.JsonEquivalent

class UserDetailInput(
    val displayName: String? = null,
    val email: String? = null,
    val emailVerified: Boolean? = false,
    val isAnonymous: Boolean? = true,
    val phoneNumber: String? = null,
    val photoURL: String? = null,
    val providerId: String? = null,
    val providerUid: String? = null,
    val firebaseUid: String? = null,
) : JsonEquivalent