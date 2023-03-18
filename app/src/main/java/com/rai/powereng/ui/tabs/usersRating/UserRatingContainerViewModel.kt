package com.rai.powereng.ui.tabs.usersRating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.usecase.auth.GetAuthState

class UserRatingContainerViewModel(
    private val getAuthState: GetAuthState
) : ViewModel() {

    init {
        getAuthStateResponse()
    }

    fun getAuthStateResponse() = getAuthState.invoke(viewModelScope)
}