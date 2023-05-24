package com.rai.powereng.usecase.auth

import com.rai.powereng.repository.FirebaseAuthRepository

class SignInWithGoogle(private val repo: FirebaseAuthRepository) {
    suspend operator fun invoke(idToken: String) = repo.firebaseSignInWithGoogle(idToken)
}