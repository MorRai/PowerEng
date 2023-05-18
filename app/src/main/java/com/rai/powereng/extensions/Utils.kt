package com.rai.powereng.extensions

fun getLevel(score: Int): Int {
    val starsPoint = 100
    val level = (score / starsPoint) + 1
    return if (level <= 20) {
        level
    } else {
        20
    }
}

fun getTimeSting(elapsedSeconds: Long): String {
    val elapsedMinutes = elapsedSeconds / 60
    val remainingSeconds = elapsedSeconds % 60
    return String.format("%d:%02d", elapsedMinutes, remainingSeconds)
}