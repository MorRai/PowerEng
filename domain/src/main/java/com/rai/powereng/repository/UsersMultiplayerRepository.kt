package com.rai.powereng.repository

import com.rai.powereng.model.Response
import com.rai.powereng.model.UserMultiplayer
import kotlinx.coroutines.flow.Flow

interface UsersMultiplayerRepository {
    fun showAnswers(gameCode: String): Flow<Response<List<UserMultiplayer>>>
    suspend fun createGame(gameCode: String, playerName: String, playerImage:String): Response<Boolean>
    suspend fun joinGame(gameCode: String, playerName: String,playerImage:String): Response<Boolean>
    suspend fun generateGameCode(): Response.Success<String>
    fun waitForPlayersToJoin(gameCode: String): Flow<Response<Boolean>>
    suspend fun saveAnswers(numCorrectAnswers: Int,gameCode:String, playerName:String, playerImage:String, startTime:Long, isComplete:Boolean,isForDelete: Boolean)
    suspend fun cancelGame(gameCode: String): Response<Boolean>
}