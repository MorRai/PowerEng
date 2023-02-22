package com.rai.powereng.usecase.auth

import com.rai.powereng.repository.FirebaseAuthRepository

class SignUpWithEmailPassword(
    private val repo: FirebaseAuthRepository,
    private val name: String,
    private val email: String,
    private val password: String
) {
    suspend operator fun invoke() = repo.signUpWithEmailPassword(name, email, password )
}