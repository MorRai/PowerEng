package com.rai.powereng.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rai.powereng.database.UserProgressInfoDatabase
import com.rai.powereng.mapper.toDomainModels
import com.rai.powereng.model.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

internal class UserProgressInfoRepositoryImpl(
    private val db: FirebaseFirestore,
    private val userProgressInfoDatabase: UserProgressInfoDatabase,
) : UserProgressInfoRepository {

    override suspend fun addUserProgressInfo(userProgressInfo: UserProgressInfo): Response<Boolean> {
        return try {
            val id = "unit ${userProgressInfo.unitId} part ${userProgressInfo.partId}"
            val response = db.collection("tasks")
                .document(id)
                .get()
                .await().toObject(UserProgressInfo::class.java)
            if (response == null || response.points <= userProgressInfo.points){
                db.collection("usersProgressInfo")
                    .document(id)
                    .set(userProgressInfo).await()
                refreshUserScore(userProgressInfo.userId)
            }
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

    override suspend fun refreshUserScore(currentUserId:String): Response<Boolean> {
        return try {
            val response = db.collection("tasks")
                .whereEqualTo("userId", currentUserId)
                .get()
                .await().documents.mapNotNull { snapShot ->
                    snapShot.toObject(UserScore::class.java)
                }
            val sumScore = response.sumOf { it.score }
            val userScore = UserScore(score=sumScore, userId = currentUserId)
            db.collection("usersScore")
                .document(currentUserId)
                .set(userScore).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }


     override fun getUsersScore() = callbackFlow{
         val snapshotListener = db.collection("usersScore").addSnapshotListener { snapshot, e ->
             val usersScoreResponse = if (snapshot != null) {
                 val usersScore = snapshot.toObjects(UserScore::class.java)
                 Response.Success(usersScore)
             } else {
                 Response.Failure(e ?:  Exception("Unknown error"))
             }
             trySend(usersScoreResponse)
         }
         awaitClose {
             snapshotListener.remove()
         }
    }

    override suspend fun getYourScore(currentUserId:String): Response<UserScore> {
        return try {
            val response = db.collection("usersScore")
                .document(currentUserId)
                .get()
                .await().toObject(UserScore::class.java)
            if (response != null){
                Response.Success(response)}
            else{
                throw  Exception("No date about your score")
            }
        } catch (exception: Exception) {
            Response.Failure(exception)
        }
    }

}