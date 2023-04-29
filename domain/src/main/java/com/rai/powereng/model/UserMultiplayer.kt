package com.rai.powereng.model

data class UserMultiplayer(
    val name: String = "",
    var score: Int = 0,
    var time: Long = 0,
    var isComplete: Boolean = false,
    val image: String = ""
){
    // иначе почемуто файр бейс не хочет работать, но схуяли?
    // геттер для isComplete
    fun getIsComplete(): Boolean {
        return isComplete
    }

    // сеттер для isComplete
    fun setIsComplete(isComplete: Boolean) {
        this.isComplete = isComplete
    }
}