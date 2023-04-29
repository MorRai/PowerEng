package com.rai.powereng.ui.partTasks.multiplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.repository.UsersMultiplayerRepository
import com.rai.powereng.usecase.CreateGameUseCase
import com.rai.powereng.usecase.JoinGameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConnectionCodeViewModel( private val multiplayerRepository: UsersMultiplayerRepository,
                               private val joinGame: JoinGameUseCase,
                               private val createGame: CreateGameUseCase
): ViewModel()  {

    private val _responseFlow = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val responseFlow: StateFlow<Response<Boolean>> = _responseFlow

    private val _responseCancelFlow = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val responseCancelFlow: StateFlow<Response<Boolean>> = _responseCancelFlow

    var isSearchingGame = false



    fun createGame(gameCode: String) {
        viewModelScope.launch {
            createGame.invoke(gameCode)
        }
        isSearchingGame = true
    }

    fun joinGame(gameCode: String) {
        _responseFlow.value = Response.Loading
        viewModelScope.launch {
            val result = joinGame.invoke(gameCode)
            _responseFlow.value = result
        }
    }

    fun cancelGame(gameCode: String) {
        _responseCancelFlow.value = Response.Loading
        viewModelScope.launch {
            val result = multiplayerRepository.cancelGame(gameCode)
            _responseCancelFlow.value = result
        }
        isSearchingGame = false
    }



    fun waitForPlayersToJoin(gameCode: String) =  multiplayerRepository.waitForPlayersToJoin(gameCode)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Response.Loading
        )
}