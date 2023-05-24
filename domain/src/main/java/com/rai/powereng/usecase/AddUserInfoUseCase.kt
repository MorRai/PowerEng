package com.rai.powereng.usecase

import com.rai.powereng.model.Response
import com.rai.powereng.model.UserProgressInfo
import com.rai.powereng.repository.FirebaseAuthRepository
import com.rai.powereng.repository.UserProgressInfoRepository

class AddUserInfoUseCase(
    private val repoUserInfo: UserProgressInfoRepository,
    private val repoAuth: FirebaseAuthRepository,
) {
    suspend operator fun invoke(userProgressInfo: UserProgressInfo): Response<Boolean> {
        return if (repoAuth.isUserAuthenticatedInFirebase && userProgressInfo.userId != "") {
            repoUserInfo.addUserProgressInfo(userProgressInfo)
        } else {
            repoUserInfo.addUserProgressInfoLocal(userProgressInfo)
        }
    }
}