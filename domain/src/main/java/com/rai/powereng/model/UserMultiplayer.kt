package com.rai.powereng.model

data class UserMultiplayer(
    val name: String = "",
    var score: Int = 0,
    var time: Long = 0,
    var isComplete: Boolean = false,
    var isForDelete: Boolean = false,
    val image: String = "",
) {
    // otherwise, for some reason, firebase does not want to work, but why? (firebase bug)
    fun getIsComplete(): Boolean {
        return isComplete
    }

    fun setIsComplete(isComplete: Boolean) {
        this.isComplete = isComplete
    }

    fun getIsForDelete(): Boolean {
        return isForDelete
    }

    fun setIsForDelete(isForDelete: Boolean) {
        this.isForDelete = isForDelete
    }
}