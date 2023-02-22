package com.rai.powereng.usecase.auth

import com.rai.powereng.repository.FirebaseAuthRepository

class SignOut (private val repo: FirebaseAuthRepository){
    suspend operator fun invoke() = repo.signOut()
}