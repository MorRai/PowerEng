package com.rai.powereng.ui.partTasks.multiplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.usecase.GetScoreGameUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class GameResultViewModel( private val getScoreGameUseCase: GetScoreGameUseCase): ViewModel()   {

    fun getAnswers(gameCode: String) =  getScoreGameUseCase.invoke(gameCode)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Response.Loading
        )
}