package com.rai.powereng.ui.tabs.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.model.User
import com.rai.powereng.usecase.GetUserScoreUseCase
import com.rai.powereng.usecase.auth.GetCurrentUser
import com.rai.powereng.usecase.auth.SignOut
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileContentViewModel(
    getUserScoreUseCase: GetUserScoreUseCase,
    private val signOut: SignOut,
    getCurrentUser: GetCurrentUser,
) : ViewModel() {

    private val _signOutResponse = MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val signOutResponse: StateFlow<Response<Boolean>> = _signOutResponse

    val userScoreFlow = getUserScoreUseCase.invoke()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Response.Loading
        )

    val userAuthFlow: StateFlow<User?> = getCurrentUser.invoke(viewModelScope)

    fun signOutUser() = viewModelScope.launch {
        _signOutResponse.value = Response.Loading
        val result = signOut.invoke()
        _signOutResponse.value = result
    }
}