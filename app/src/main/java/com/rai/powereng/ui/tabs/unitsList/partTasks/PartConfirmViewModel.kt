package com.rai.powereng.ui.tabs.unitsList.partTasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.User
import com.rai.powereng.usecase.auth.GetCurrentUser
import kotlinx.coroutines.flow.StateFlow

class PartConfirmViewModel(private val getCurrentUser: GetCurrentUser) :ViewModel() {
    fun geCurrentUserResponse(): StateFlow<User?> = getCurrentUser.invoke(viewModelScope)
}