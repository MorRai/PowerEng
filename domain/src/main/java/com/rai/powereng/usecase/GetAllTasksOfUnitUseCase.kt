package com.rai.powereng.usecase

import com.rai.powereng.repository.TasksRepository

class GetAllTasksOfUnitUseCase(private val repoTasks: TasksRepository) {

    operator fun invoke(unitId: Int) = repoTasks.getAllTasksOfUnit(unitId)
}