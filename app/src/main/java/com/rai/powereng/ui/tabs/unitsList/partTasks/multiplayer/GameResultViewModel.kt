package com.rai.powereng.ui.tabs.unitsList.partTasks.multiplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.usecase.auth.GetCurrentUser
import com.rai.powereng.usecase.multiplayer.GetScoreGameUseCase
import com.rai.powereng.usecase.multiplayer.SaveGameUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class GameResultViewModel(
    private val getScoreGameUseCase: GetScoreGameUseCase,
    private val getCurrentUser: GetCurrentUser,
    private val saveGameUseCase: SaveGameUseCase
) : ViewModel() {

    init {
        getCurrentUserResponse()
    }

    fun getCurrentUserResponse() = getCurrentUser.invoke(viewModelScope)

    fun saveAnswers(
        gameCode: String,
        numCorrectAnswers: Int,
        startTime: Long,
        isComplete: Boolean,
        isForDelete: Boolean = true,
    ) {
        saveGameUseCase.invoke(numCorrectAnswers, gameCode, startTime, isComplete, isForDelete)
    }


    fun getAnswers(gameCode: String) = getScoreGameUseCase.invoke(gameCode)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Response.Loading
        )
}