package com.rai.powereng.repository

import com.rai.powereng.model.Response
import com.rai.powereng.model.UserMultiplayer
import kotlinx.coroutines.flow.Flow

interface UsersMultiplayerRepository {

    fun showAnswers(gameCode: String): Flow<Response<List<UserMultiplayer>>>

    suspend fun createGame(gameCode: String, playerName: String)

    suspend fun joinGame(gameCode: String, playerName: String): Response<Boolean>

    fun waitForPlayersToJoin(gameCode: String): Flow<Response<Boolean>>

    suspend fun saveAnswers(numCorrectAnswers: Int,gameCode:String, playerName:String, startTime:Long)
    suspend fun cancelGame(gameCode: String): Response<Boolean>

}