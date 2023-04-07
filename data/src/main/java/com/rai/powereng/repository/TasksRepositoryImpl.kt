package com.rai.powereng.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rai.powereng.model.Response
import com.rai.powereng.model.TaskData
import kotlinx.coroutines.tasks.await

internal class TasksRepositoryImpl(private val db: FirebaseFirestore) : TasksRepository {

    override suspend fun getTasksData(unitId: Int, partId: Int, taskNum: Int): Response<TaskData> {
        return try {
            val response = db.collection("tasks")
                .whereEqualTo("unit", unitId)
                .whereEqualTo("part", partId)
                .whereEqualTo("taskNum", taskNum)
                .get()
                .await().documents.mapNotNull { snapShot ->
                    snapShot.toObject(TaskData::class.java)
                }
            Response.Success(response[0])//возвращаем первый, конечно не красиво как то
        } catch (exception: Exception) {
            Response.Failure(exception)
        }
    }

    override suspend fun getAmountTasksInPart(unitId: Int, partId: Int): Response<Int> {
        return try {
            val response = db.collection("tasks")
                .whereEqualTo("unit", unitId)
                .whereEqualTo("part", partId)
                .get()
                .await().documents.size
            Response.Success(response)
        } catch (exception: Exception) {
            Response.Failure(exception)
        }
    }
}
