package com.rai.powereng.mapper

import com.rai.powereng.model.UserProgressInfo
import com.rai.powereng.model.UserProgressInfoEntity


internal fun UserProgressInfoEntity.toDomainModels(currentUserId:String): UserProgressInfo {
    return UserProgressInfo(
        userId = currentUserId,
        points = points,
        data = data,
        mistakes = mistakes,
        unitId = unitId,
        partId = partId,
    )
}

internal fun UserProgressInfo.toDomainModels() : UserProgressInfoEntity{
    return UserProgressInfoEntity(
        id = "$unitId$partId".toInt(),
        userId = userId,
        points = points,
        data = data,
        mistakes = mistakes,
        unitId = unitId,
        partId = partId,
    )
}
