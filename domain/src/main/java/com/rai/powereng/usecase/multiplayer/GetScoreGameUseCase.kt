package com.rai.powereng.usecase.multiplayer

import com.rai.powereng.repository.UsersMultiplayerRepository

class GetScoreGameUseCase(private val repoMultiplayer: UsersMultiplayerRepository) {
    operator fun invoke(gameCode: String) = repoMultiplayer.showAnswers(gameCode)
}