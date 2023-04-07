package com.rai.powereng.usecase

import com.rai.powereng.repository.FirebaseAuthRepository

class GetUserIdUseCase( private val repo: FirebaseAuthRepository) {

    operator fun invoke() = repo.currentUserId
}