package com.rai.powereng.usecase

import com.rai.powereng.model.UserProgressInfo
import com.rai.powereng.repository.UserProgressInfoRepository

class AddUserInfoUseCase(private val repoUserInfo: UserProgressInfoRepository) {

    suspend operator fun invoke(userProgressInfo: UserProgressInfo) = repoUserInfo.addUserProgressInfo(userProgressInfo)
}