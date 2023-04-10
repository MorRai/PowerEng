package com.rai.powereng.model

data class UserScore (
        val userId: String="",
        val score:Int = 0,
        val daysStrike:Int = 0,
        val dateLastCompleteTask :String = ""
        )