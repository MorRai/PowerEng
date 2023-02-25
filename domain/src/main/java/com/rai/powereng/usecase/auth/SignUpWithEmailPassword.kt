package com.rai.powereng.usecase.auth

import com.rai.powereng.model.Response
import com.rai.powereng.repository.FirebaseAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignUpWithEmailPassword(
    private val repo: FirebaseAuthRepository,
) {
    suspend operator fun invoke(email:String, password:String)  = repo.signUpWithEmailPassword( email, password )
}