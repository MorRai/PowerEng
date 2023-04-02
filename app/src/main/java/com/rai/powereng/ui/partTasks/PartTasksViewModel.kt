package com.rai.powereng.ui.partTasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.model.TaskData
import com.rai.powereng.usecase.GetAmountTasksInPartUseCase
import com.rai.powereng.usecase.GetTasksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PartTasksViewModel(private val getTasksUseCase: GetTasksUseCase,
                         private val getAmountTasksInPartUseCase: GetAmountTasksInPartUseCase
): ViewModel()  {

    private val _tasksFlow = MutableStateFlow<Response<TaskData>>(Response.Loading)
    val tasksFlow: StateFlow<Response<TaskData>> = _tasksFlow

    private val _tasksAmountFlow = MutableStateFlow<Response<Int>>(Response.Success(0))
    val tasksAmountFlow: StateFlow<Response<Int>> = _tasksAmountFlow


    fun getTasksUser(unitId: Int, partId: Int, taskNum: Int) = viewModelScope.launch {
        _tasksFlow.value = Response.Loading
        val result = getTasksUseCase.invoke(unitId,partId, taskNum)
        _tasksFlow.value = result
    }


    fun getTasksAmount(unitId: Int, partId: Int) = viewModelScope.launch {
        _tasksAmountFlow.value = Response.Loading
        val result = getAmountTasksInPartUseCase.invoke(unitId,partId)
        _tasksAmountFlow.value = result
    }
}