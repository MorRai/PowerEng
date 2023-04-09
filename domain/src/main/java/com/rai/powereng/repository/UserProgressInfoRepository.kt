package com.rai.powereng.repository

import com.rai.powereng.model.Response
import com.rai.powereng.model.UserProgressInfo
import com.rai.powereng.model.UserScore
import kotlinx.coroutines.flow.Flow

interface UserProgressInfoRepository {
    suspend fun addUserProgressInfo(userProgressInfo: UserProgressInfo): Response<Boolean>
    suspend fun addUserProgressInfoLocal(userProgressInfo: UserProgressInfo): Response<Boolean>
    suspend fun transferUserProgressInfo(currentUserId:String): Response<Boolean>
    suspend fun refreshUserScore(currentUserId:String): Response<Boolean>
     fun getUsersScore(): Flow<Response<List<UserScore>>>
    suspend fun getYourScore(currentUserId:String): Response<UserScore>
}