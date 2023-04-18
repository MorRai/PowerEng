package com.rai.powereng.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.usecase.auth.GetCurrentUser

class SplashViewModel(private val getCurrentUser: GetCurrentUser
) : ViewModel() {

    init {
        geCurrentUserResponse()
    }
    fun geCurrentUserResponse() =  getCurrentUser.invoke(viewModelScope)

}