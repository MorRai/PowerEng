package com.rai.powereng.ui.partTasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.model.UserProgressInfo
import com.rai.powereng.usecase.AddUserInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PartTasksFinishViewModel(private val addUserInfoUseCase: AddUserInfoUseCase) : ViewModel()  {

    private val _taddUserInfoFlow = MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val addUserInfoFlow: StateFlow<Response<Boolean>> = _taddUserInfoFlow

    fun addUserInfo(userProgressInfo: UserProgressInfo) = viewModelScope.launch {
        _taddUserInfoFlow.value = Response.Loading
        _taddUserInfoFlow.value  = addUserInfoUseCase.invoke(userProgressInfo)
    }
}