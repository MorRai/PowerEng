package com.rai.powereng.usecase.auth

import com.rai.powereng.repository.FirebaseAuthRepository

class SignInWithEmailPassword(
private val repo: FirebaseAuthRepository,
private val email: String,
private val password: String
) {
    suspend operator fun invoke() = repo.signInWithEmailPassword(email, password )
}