package com.rai.powereng.model

data class TaskData(
    val unit: Int = 0,
    val part: Int = 0,
    val typeTask: Int = 0,
    val taskNum: Int = 0,
    val question: String = "",
    val answer: String = "",
    val variants: String = "",
    val forInfoUnit: String = "",
)
