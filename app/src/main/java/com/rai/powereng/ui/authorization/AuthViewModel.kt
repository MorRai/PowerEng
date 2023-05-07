package com.rai.powereng.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.repository.UserProgressInfoRepository
import com.rai.powereng.usecase.auth.GetCurrentUser
import kotlinx.coroutines.launch


class AuthViewModel(private val getCurrentUser: GetCurrentUser,
                    private val userProgressInfoRepository: UserProgressInfoRepository
): ViewModel() {

    init {
        getCurrentUserResponse()
    }

    fun updateUserInfo() = viewModelScope.launch {
        val userId =getCurrentUserResponse().value?.uid
        if (userId != null) {
            userProgressInfoRepository.refreshUserScore(userId, true)
        }
    }


    fun getCurrentUserResponse() =  getCurrentUser.invoke(viewModelScope)

}