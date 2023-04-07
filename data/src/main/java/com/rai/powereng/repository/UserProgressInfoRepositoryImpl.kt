package com.rai.powereng.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rai.powereng.database.UserProgressInfoDatabase
import com.rai.powereng.mapper.toDomainModels
import com.rai.powereng.model.Response
import com.rai.powereng.model.UserProgressInfo
import kotlinx.coroutines.tasks.await

internal class UserProgressInfoRepositoryImpl(
    private val db: FirebaseFirestore,
    private val userProgressInfoDatabase: UserProgressInfoDatabase,
) : UserProgressInfoRepository {

    override suspend fun addUserProgressInfo(userProgressInfo: UserProgressInfo): Response<Boolean> {
        return try {
            val id = db.collection("usersProgressInfo").document().id
            db.collection("usersProgressInfo")
                .document(id)
                .set(userProgressInfo).await()
            Response.Success(true)
        } catch (e: Exception) {
            try {
                addUserProgressInfoLocal(userProgressInfo)
                Response.Success(true)
            } catch (e: Exception) {
                Response.Failure(e)
            }

        }
    }

    override suspend fun addUserProgressInfoLocal(userProgressInfo: UserProgressInfo): Response<Boolean> {
        return try {
            userProgressInfoDatabase.userProgressInfoDao().insert(userProgressInfo.toDomainModels())
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun transferUserProgressInfo(currentUserId:String): Response<Boolean> {
        return try {
            userProgressInfoDatabase.userProgressInfoDao().getAll().map {it.toDomainModels(currentUserId)}
                .forEach {
                    addUserProgressInfo(it)
                }
            userProgressInfoDatabase.userProgressInfoDao().getAll()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

}