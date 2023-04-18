package com.rai.powereng.usecase.auth

import com.rai.powereng.repository.FirebaseAuthRepository

class SignInWithEmailPassword(
private val repo: FirebaseAuthRepository
) {
    suspend operator fun invoke(email:String, password:String) =
        repo.signInWithEmailPassword(email, password )
}