package com.rai.powereng.usecase.auth

import com.rai.powereng.model.Response
import com.rai.powereng.repository.FirebaseAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInWithEmailPassword(
private val repo: FirebaseAuthRepository
) {
     operator fun invoke(email:String, password:String): Flow<Response<Boolean>> = flow{
        repo.signInWithEmailPassword(email, password )}
}