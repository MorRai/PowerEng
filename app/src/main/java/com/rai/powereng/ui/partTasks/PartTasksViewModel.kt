package com.rai.powereng.ui.partTasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.model.TaskData
import com.rai.powereng.usecase.GetTasksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PartTasksViewModel(private val getTasksUseCase: GetTasksUseCase,
): ViewModel()  {

    private val _tasksFlow = MutableStateFlow<Response<TaskData>>(Response.Loading)
    val tasksFlow: StateFlow<Response<TaskData>> = _tasksFlow



    fun getTasksUser(unitId: Int, partId: Int, taskNum: Int) = viewModelScope.launch {
        _tasksFlow.value = Response.Loading
        val result = getTasksUseCase.invoke(unitId,partId, taskNum)
        _tasksFlow.value = result
    }
}