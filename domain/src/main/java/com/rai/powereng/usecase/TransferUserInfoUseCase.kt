package com.rai.powereng.usecase

import com.rai.powereng.model.Response
import com.rai.powereng.repository.FirebaseAuthRepository
import com.rai.powereng.repository.UserProgressInfoRepository

class TransferUserInfoUseCase(
    private val repoUserInfo: UserProgressInfoRepository,
    private val repoAuth: FirebaseAuthRepository
) {

    suspend operator fun invoke(): Response<Boolean> {
        return if (repoAuth.isUserAuthenticatedInFirebase) {
            repoUserInfo.transferUserProgressInfo(repoAuth.currentUserId)
        } else {
            Response.Success(false)
        }
    }
}