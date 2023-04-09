package com.rai.powereng.usecase

import com.rai.powereng.repository.UserProgressInfoRepository

class GetUsersScoreUseCase(private val repoUserInfo: UserProgressInfoRepository) {

     operator fun invoke() = repoUserInfo.getUsersScore()

}