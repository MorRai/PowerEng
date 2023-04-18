package com.rai.powereng.usecase.auth

import com.rai.powereng.repository.FirebaseAuthRepository
import kotlinx.coroutines.CoroutineScope

class GetCurrentUser(private val repo: FirebaseAuthRepository) {
     operator fun invoke(viewModelScope: CoroutineScope) = repo.getCurrentUser(viewModelScope)
}