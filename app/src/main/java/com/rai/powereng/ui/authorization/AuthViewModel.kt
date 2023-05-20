package com.rai.powereng.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.usecase.auth.GetCurrentUser

class AuthViewModel(private val getCurrentUser: GetCurrentUser) : ViewModel() {
    init {
        getCurrentUserResponse()
    }
    fun getCurrentUserResponse() = getCurrentUser.invoke(viewModelScope)
}