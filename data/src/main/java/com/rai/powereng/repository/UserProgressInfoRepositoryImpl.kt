package com.rai.powereng.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rai.powereng.database.UserProgressInfoDatabase
import com.rai.powereng.mapper.toDomainModels
import com.rai.powereng.model.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

internal class UserProgressInfoRepositoryImpl(
    private val db: FirebaseFirestore,
    private val userProgressInfoDatabase: UserProgressInfoDatabase,
) : UserProgressInfoRepository {

    override suspend fun addUserProgressInfo(userProgressInfo: UserProgressInfo): Response<Boolean> {
        return try {
            val id = "unit${userProgressInfo.unitId}part${userProgressInfo.partId}"
            val response = db.collection("tasks")
                .document(id)
                .get()
                .await().toObject(UserProgressInfo::class.java)
            if (response == null || response.points <= userProgressInfo.points) {
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

    override suspend fun transferUserProgressInfo(currentUserId: String): Response<Boolean> {
        return try {
            userProgressInfoDatabase.userProgressInfoDao().getAll()
                .map { it.toDomainModels(currentUserId) }
                .forEach {
                    addUserProgressInfo(it)
                }
            userProgressInfoDatabase.userProgressInfoDao().getAll()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun refreshUserScore(currentUserId: String): Response<Boolean> {
        return try {
            val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val dateText = dateFormat.format(Date())

            val response = db.collection("usersProgressInfo")
                .whereEqualTo("userId", currentUserId)
                .get()
                .await().documents.mapNotNull { snapShot ->
                    snapShot.toObject(UserProgressInfo::class.java)
                }

            val groupedData = response.groupBy { it.unitId }
            val maxUnit = groupedData.keys.maxOrNull() ?: 0
            val maxPart = groupedData[maxUnit]?.maxByOrNull { it.partId }?.partId ?: 0
            val sumScore = response.sumOf { it.points }

            val oldUserScore = db.collection("usersScore")
                .document(currentUserId)
                .get()
                .await().toObject(UserScore::class.java)

            val userScore = when {
                oldUserScore == null || getDifferenceDays(oldUserScore.dateLastCompleteTask, dateText) > 1 -> {
                    UserScore(
                        score = sumScore,
                        userId = currentUserId,
                        dateLastCompleteTask = dateText,
                        daysStrike = 1,
                        unit = maxUnit,
                        part = maxPart
                    )
                }
                oldUserScore.dateLastCompleteTask == dateText -> {
                    UserScore(
                        score = sumScore,
                        userId = currentUserId,
                        dateLastCompleteTask = oldUserScore.dateLastCompleteTask,
                        daysStrike = oldUserScore.daysStrike,
                        unit = maxUnit,
                        part = maxPart
                    )
                }
                else -> {
                    UserScore(
                        score = sumScore,
                        userId = currentUserId,
                        dateLastCompleteTask = dateText,
                        daysStrike = oldUserScore.daysStrike + 1,
                        unit = maxUnit,
                        part = maxPart
                    )
                }
            }
            db.collection("usersScore")
                .document(currentUserId).set(userScore).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }


    override fun getUsersScore() = callbackFlow {
        val snapshotListener = db.collection("usersScore").addSnapshotListener { snapshot, e ->
            val usersScoreResponse = if (snapshot != null) {
                val usersScore = snapshot.toObjects(UserScore::class.java)
                Response.Success(usersScore)
            } else {
                Response.Failure(e ?: Exception("Unknown error"))
            }
            trySend(usersScoreResponse)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getYourScore(currentUserId: String) = callbackFlow {
        val snapshotListener = db.collection("usersScore")
            .document(currentUserId)
            .addSnapshotListener { snapshot, e ->
                val userScoreResponse = if (snapshot != null) {
                    val userScore = snapshot.toObject(UserScore::class.java)
                    if (userScore != null) {
                        Response.Success(userScore)
                    } else {
                        Response.Failure(e ?: Exception("Unknown error"))
                    }
                } else {
                    Response.Failure(e ?: Exception("Unknown error"))
                }
                trySend(userScoreResponse)
            }
        awaitClose {
            snapshotListener.remove()
        }


    }

    private fun getDifferenceDays(dateFirst: String, dateSecond: String): Int {
        val dateFormatInput = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val dateFirstParse = LocalDate.parse(dateFirst, dateFormatInput)
        val dateSecondParse = LocalDate.parse(dateSecond, dateFormatInput)
        return (ChronoUnit.DAYS.between(dateFirstParse, dateSecondParse)).toInt()
    }

}