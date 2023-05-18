package com.rai.powereng.repository

import com.rai.powereng.model.*
import kotlinx.coroutines.flow.Flow

interface UserProgressInfoRepository {
    suspend fun addUserProgressInfo(userProgressInfo: UserProgressInfo): Response<Boolean>
    suspend fun addUserProgressInfoLocal(userProgressInfo: UserProgressInfo): Response<Boolean>
    suspend fun transferUserProgressInfo(currentUserId:String): Response<Boolean>
    suspend fun refreshUserScore(currentUserId:String, itStart:Boolean = false): Response<Boolean>
     fun getUsersScore(): Flow<Response<List<UserScoreWithProfile>>>
     fun getYourScore(currentUserId:String): Flow<Response<UserScore>>
}