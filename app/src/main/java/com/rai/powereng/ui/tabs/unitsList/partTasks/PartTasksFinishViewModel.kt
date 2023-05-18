package com.rai.powereng.ui.tabs.unitsList.partTasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.model.UserProgressInfo
import com.rai.powereng.usecase.AddUserInfoUseCase
import com.rai.powereng.usecase.GetUserIdUseCase
import com.rai.powereng.usecase.TransferUserInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PartTasksFinishViewModel(private val addUserInfoUseCase: AddUserInfoUseCase,
                               private val transferUserInfoUseCase : TransferUserInfoUseCase,
                               private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel()  {
    init {
        transferUserInfo()
    }

    private val _taddUserInfoFlow = MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val addUserInfoFlow: StateFlow<Response<Boolean>> = _taddUserInfoFlow

    fun addUserInfo(points:Int,dateText:String,mistakes:Int,unitId:Int,partId:Int) = viewModelScope.launch {
        _taddUserInfoFlow.value = Response.Loading
        val userId = getUserIdUseCase.invoke()
        val userInfo = UserProgressInfo(userId,points,dateText,mistakes,unitId,partId)
        _taddUserInfoFlow.value = addUserInfoUseCase.invoke(userInfo)
    }

    private fun transferUserInfo() = viewModelScope.launch {
        transferUserInfoUseCase.invoke()
    }

}