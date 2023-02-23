package com.rai.powereng.usecase.auth

import com.rai.powereng.repository.FirebaseAuthRepository
import kotlinx.coroutines.CoroutineScope

class GetAuthState( private val repo: FirebaseAuthRepository) {
     operator fun invoke(viewModelScope: CoroutineScope) = repo.getFirebaseAuthState(viewModelScope)
}