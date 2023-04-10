package com.rai.powereng.ui.tabs.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.usecase.GetUserScoreUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ProfileContentViewModel(getUserScoreUseCase: GetUserScoreUseCase):ViewModel() {

    val userScoreFlow = getUserScoreUseCase.invoke()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Response.Loading
        )
}