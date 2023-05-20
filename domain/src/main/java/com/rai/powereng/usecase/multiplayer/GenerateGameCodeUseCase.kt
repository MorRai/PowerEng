package com.rai.powereng.usecase.multiplayer

import com.rai.powereng.repository.UsersMultiplayerRepository

class GenerateGameCodeUseCase( private val repoMultiplayer: UsersMultiplayerRepository) {
    suspend operator fun invoke(postfix:String) = repoMultiplayer.generateGameCode(postfix)
}