package com.rai.powereng.ui.partTasks.multiplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.repository.UsersMultiplayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConnectionCodeViewModel( private val multiplayerRepository: UsersMultiplayerRepository
): ViewModel()  {

    private val _responseFlow = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val responseFlow: StateFlow<Response<Boolean>> = _responseFlow

    fun createGame(gameCode: String, playerName: String) {
        viewModelScope.launch {
            multiplayerRepository.createGame(gameCode, playerName)
        }
    }

    fun joinGame(gameCode: String, playerName: String) {
        _responseFlow.value = Response.Loading
        viewModelScope.launch {
            val result = multiplayerRepository.joinGame(gameCode, playerName)
            _responseFlow.value = result
        }
    }

    fun waitForPlayersToJoin(gameCode: String) =  multiplayerRepository.waitForPlayersToJoin(gameCode)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Response.Loading
        )
}