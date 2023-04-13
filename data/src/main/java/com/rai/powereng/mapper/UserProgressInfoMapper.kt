package com.rai.powereng.mapper

import com.google.firebase.auth.FirebaseUser
import com.rai.powereng.model.User
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
        id = "unit $unitId part $partId",
        userId = userId,
        points = points,
        data = data,
        mistakes = mistakes,
        unitId = unitId,
        partId = partId,
    )
}

internal fun FirebaseUser.toDomainModels() : User {
    return User(
         uid = uid,
         displayName= displayName,
         email = email,
         photoUrl = photoUrl.toString(),
         registrationTimeMillis = metadata?.creationTimestamp,
         isEmailVerified = isEmailVerified
    )
}
