package com.rai.powereng.model

data class UserProgressInfo(
    val usersAppId: String = "",
    val user: String="",
    val points: Int=0,
    val data: String = "",
    val mistakes: Int=0,
    val unitId:Int =0,
    val partId:Int=0,
)