package com.rai.powereng.usecase.auth

import com.rai.powereng.repository.FirebaseAuthRepository

class SendPasswordReset(
    private val repo: FirebaseAuthRepository,
) {
    suspend operator fun invoke(email: String) = repo.sendPasswordReset(email)
}