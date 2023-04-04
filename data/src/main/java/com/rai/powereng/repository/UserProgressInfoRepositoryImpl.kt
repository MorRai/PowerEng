package com.rai.powereng.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rai.powereng.model.Response
import com.rai.powereng.model.UserProgressInfo
import kotlinx.coroutines.tasks.await

class UserProgressInfoRepositoryImpl(
    private val db: FirebaseFirestore
):UserProgressInfoRepository {

    override suspend fun addUserProgressInfo(userProgressInfo: UserProgressInfo): Response<Boolean> {
        return try {
            val id =   db.collection("usersProgressInfo").document().id
            db.collection("usersProgressInfo")
            .document(id)
            .set(userProgressInfo).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

}