package com.rai.powereng.usecase.auth

import com.rai.powereng.repository.FirebaseAuthRepository

class SendPasswordReset(
    private val repo: FirebaseAuthRepository,
    private val email: String
) {
    suspend operator fun invoke() = repo.sendPasswordReset(email)
}