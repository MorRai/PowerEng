package com.rai.powereng.model

data class UserScoreWithProfile(
    val userId: String="",
    val score:Int = 0,
    val daysStrike:Int = 0,
    val displayName: String? = "",
    val photoUrl: String?= ""
)