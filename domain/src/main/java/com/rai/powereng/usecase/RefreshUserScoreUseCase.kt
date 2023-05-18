package com.rai.powereng.usecase

import com.rai.powereng.repository.UserProgressInfoRepository

class RefreshUserScoreUseCase(private val userProgressInfoRepository: UserProgressInfoRepository) {

    suspend operator fun invoke(userId:String, itStart:Boolean) = userProgressInfoRepository.refreshUserScore(userId, itStart)

}