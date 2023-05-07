package com.rai.powereng.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.User
import com.rai.powereng.usecase.RefreshUserScoreUseCase
import com.rai.powereng.usecase.auth.GetCurrentUser
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val getCurrentUser: GetCurrentUser,
    private val refreshUserScoreUseCase: RefreshUserScoreUseCase,
) : ViewModel() {
    fun geCurrentUserResponse(): StateFlow<User?> = getCurrentUser.invoke(viewModelScope)

    fun updateUserInfo(userId: String) = viewModelScope.launch {
        refreshUserScoreUseCase.invoke(userId, true)
    }
}