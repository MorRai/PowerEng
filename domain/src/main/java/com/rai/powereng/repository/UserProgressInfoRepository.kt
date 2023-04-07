package com.rai.powereng.repository

import com.rai.powereng.model.Response
import com.rai.powereng.model.UserProgressInfo

interface UserProgressInfoRepository {
    suspend fun addUserProgressInfo(userProgressInfo: UserProgressInfo): Response<Boolean>
    suspend fun addUserProgressInfoLocal(userProgressInfo: UserProgressInfo): Response<Boolean>
    suspend fun transferUserProgressInfo(currentUserId:String): Response<Boolean>
}