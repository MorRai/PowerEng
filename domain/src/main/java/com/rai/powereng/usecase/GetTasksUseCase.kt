package com.rai.powereng.usecase

import com.rai.powereng.repository.TasksRepository

class GetTasksUseCase(private val repoTasks: TasksRepository) {

    suspend operator fun invoke(unitId: Int, partId: Int, taskNum: Int) =
        repoTasks.getTasksData(unitId, partId, taskNum)
}