package com.rai.powereng.usecase.multiplayer

import com.rai.powereng.model.Response
import com.rai.powereng.repository.FirebaseAuthRepository
import com.rai.powereng.repository.UsersMultiplayerRepository

class CreateGameUseCase(
    private val repoMultiplayer: UsersMultiplayerRepository,
    private val repoAuth: FirebaseAuthRepository,
) {

    suspend operator fun invoke(gameCode: String): Response<Boolean> {
        return if (repoAuth.isUserAuthenticatedInFirebase) {
            repoMultiplayer.createGame(
                gameCode,
                repoAuth.currentUser?.displayName ?: "",
                repoAuth.currentUser?.photoUrl ?: "",
            )
        } else {
            Response.Failure(Exception("Пользователь не авторизирован"))
        }
    }

}