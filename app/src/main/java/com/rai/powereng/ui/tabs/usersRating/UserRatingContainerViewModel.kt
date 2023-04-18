package com.rai.powereng.ui.tabs.usersRating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.usecase.auth.GetCurrentUser

class UserRatingContainerViewModel(
    private val getCurrentUser: GetCurrentUser
) : ViewModel() {

    init {
        getCurrentUserResponse()
    }

    fun getCurrentUserResponse() = getCurrentUser.invoke(viewModelScope)
}