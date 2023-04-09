package com.rai.powereng.ui.tabs.usersRating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.usecase.GetUsersScoreUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class UsersRatingContentViewModel(getUsersScoreUseCase: GetUsersScoreUseCase):ViewModel() {

    val usersScoreFlow = getUsersScoreUseCase.invoke()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Response.Loading
        )


}