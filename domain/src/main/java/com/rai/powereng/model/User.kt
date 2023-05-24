package com.rai.powereng.model

data class User(
    val uid: String = "",
    val displayName: String? = "",
    val email: String? = "",
    val photoUrl: String? = "",
    val registrationTimeMillis: Long? = 0,
    val isEmailVerified: Boolean = false,
)