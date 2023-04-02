package com.rai.powereng.usecase

import com.rai.powereng.repository.TasksRepository

class GetAmountTasksInPartUseCase(private val repoTasks: TasksRepository) {

    suspend operator fun invoke(unitId: Int, partId: Int) = repoTasks.getAmountTasksInPart(unitId,partId)

}