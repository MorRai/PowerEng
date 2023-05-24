package com.rai.powereng.usecase

import com.rai.powereng.repository.FirebaseAuthRepository
import com.rai.powereng.repository.UserProgressInfoRepository

class GetUserScoreUseCase(
    private val repoUserInfo: UserProgressInfoRepository,
    private val repoAuth: FirebaseAuthRepository,
) {

    operator fun invoke() = repoUserInfo.getYourScore(repoAuth.currentUserId)
}