package com.rai.powereng.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.repository.UserProgressInfoRepository
import com.rai.powereng.usecase.auth.GetCurrentUser
import kotlinx.coroutines.launch

class SplashViewModel(private val getCurrentUser: GetCurrentUser,
                      private val userProgressInfoRepository: UserProgressInfoRepository
) : ViewModel() {

    init {
        updateUserInfo()
    }

    private fun updateUserInfo() = viewModelScope.launch {
        val userId = geCurrentUserResponse().value?.uid
        if (userId != null) {
            userProgressInfoRepository.refreshUserScore(userId, true)
        }
    }

    fun geCurrentUserResponse() =  getCurrentUser.invoke(viewModelScope)

}