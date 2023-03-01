package com.rai.powereng.usecase.auth

import com.rai.powereng.model.Response
import com.rai.powereng.repository.FirebaseAuthRepository

class OneTapSignInWithGoogle(private val repo: FirebaseAuthRepository) {
   // suspend operator fun <T>invoke(): Response<T> = repo.oneTapSignInWithGoogle()
}