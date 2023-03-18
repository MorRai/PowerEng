package com.rai.powereng.ui.tabs.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.usecase.auth.GetAuthState

class ProfileContainerViewModel(
    private val getAuthState: GetAuthState
) : ViewModel() {

    init {
        getAuthStateResponse()
    }

    fun getAuthStateResponse() = getAuthState.invoke(viewModelScope)
}