package com.rai.powereng.ui.tabs.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.usecase.auth.GetCurrentUser

class ProfileContainerViewModel(
    private val getCurrentUser: GetCurrentUser,
) : ViewModel() {

    init {
        getCurrentUserResponse()
    }

    fun getCurrentUserResponse() = getCurrentUser.invoke(viewModelScope)
}