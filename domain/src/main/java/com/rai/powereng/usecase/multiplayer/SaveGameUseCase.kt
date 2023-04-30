package com.rai.powereng.usecase.multiplayer

import com.rai.powereng.repository.FirebaseAuthRepository
import com.rai.powereng.repository.UsersMultiplayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaveGameUseCase(
    private val repoMultiplayer: UsersMultiplayerRepository,
    private val repoAuth: FirebaseAuthRepository,
) {

     operator fun invoke(
        numCorrectAnswers: Int,
        gameCode: String, startTime: Long,
        isComplete: Boolean,
        isForDelete: Boolean,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            if (repoAuth.isUserAuthenticatedInFirebase) {
                repoMultiplayer.saveAnswers(
                    numCorrectAnswers,
                    gameCode,
                    repoAuth.currentUser?.displayName ?: "",
                    repoAuth.currentUser?.photoUrl ?: "",
                    startTime,
                    isComplete,
                    isForDelete
                )
            }
        }
    }

}