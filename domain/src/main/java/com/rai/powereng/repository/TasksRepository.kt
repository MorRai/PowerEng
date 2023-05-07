package com.rai.powereng.repository

import com.rai.powereng.model.Response
import com.rai.powereng.model.TaskData
import kotlinx.coroutines.flow.Flow

interface TasksRepository {

    suspend fun getTasksData(unitId: Int, partId: Int, taskNum: Int): Response<TaskData>
    fun getAllTasksOfUnit(unitId: Int): Flow<Response<List<TaskData>>>
    suspend fun getAmountTasksInPart(unitId: Int,partId: Int): Response<Int>

}