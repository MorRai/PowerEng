package com.rai.powereng.usecase

import com.rai.powereng.repository.FirebaseAuthRepository
import com.rai.powereng.repository.UsersMultiplayerRepository

class CreateGameUseCase(
    private val repoMultiplayer: UsersMultiplayerRepository,
    private val repoAuth: FirebaseAuthRepository,
) {

    suspend operator fun invoke(gameCode: String) {
        if (repoAuth.isUserAuthenticatedInFirebase) {
            repoMultiplayer.createGame(
                gameCode,
                repoAuth.currentUser?.displayName ?: "",
                repoAuth.currentUser?.photoUrl ?: "",
            )
        }
    }

}