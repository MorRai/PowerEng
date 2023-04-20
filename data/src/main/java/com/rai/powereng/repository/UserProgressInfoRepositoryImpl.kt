package com.rai.powereng.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.rai.powereng.database.UserProgressInfoDatabase
import com.rai.powereng.mapper.toDomainModels
import com.rai.powereng.model.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
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
            val id =
                "unit${userProgressInfo.unitId}part${userProgressInfo.partId}id${userProgressInfo.userId}"
            val response = db.collection("usersProgressInfo")
                .document(id)
                .get()
                .await().toObject(UserProgressInfo::class.java)
            if (response == null || response.points <= userProgressInfo.points) { //если прошел хуже то не перезаписваем
                db.collection("usersProgressInfo")
                    .document(id)
                    .set(userProgressInfo).await()
            }
            refreshUserScore(userProgressInfo.userId, false)
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

    override suspend fun refreshUserScore(
        currentUserId: String,
        itStart: Boolean,
    ): Response<Boolean> { //itStart по умолчанию фолс
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
                itStart && (oldUserScore == null || getDifferenceDays(
                    oldUserScore.dateLastCompleteTask,
                    dateText
                ) > 1) -> {
                    UserScore(
                        score = sumScore,
                        userId = currentUserId,
                        dateLastCompleteTask = "",
                        daysStrike = 0,
                        unit = maxUnit,
                        part = maxPart
                    )
                }
                itStart -> {
                    return Response.Success(true)
                }
                oldUserScore == null || oldUserScore.dateLastCompleteTask == "" || getDifferenceDays(
                    oldUserScore.dateLastCompleteTask,
                    dateText
                ) > 1 -> {

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

    override fun getUsersScore(): Flow<Response<List<UserScoreWithProfile>>> = callbackFlow {
        val usersScoreCollection = db.collection("usersScore")
        val usersCollection = db.collection("users")
        val usersScoreListenerRegistration =
            usersScoreCollection.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    trySend(Response.Failure(e))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    launch {
                        val usersScore = snapshot.toObjects(UserScore::class.java)
                        val usersScoreWithProfiles = mutableListOf<UserScoreWithProfile>()

                        for (userScore in usersScore) {
                            val userProfile =
                                usersCollection.document(userScore.userId).get().await()
                                    .toObject<User>()
                            if (userProfile != null) {

                                usersScoreWithProfiles.add(
                                    UserScoreWithProfile(
                                        userId = userScore.userId,
                                        score = userScore.score,
                                        daysStrike = userScore.daysStrike,
                                        displayName = userProfile.displayName,
                                        photoUrl = userProfile.photoUrl
                                    )
                                )
                            }
                        }
                        var num = 0
                        var lastScore = -1//что б
                        usersScoreWithProfiles.sortedByDescending{ it.score }.forEach{
                            if (lastScore != it.score){
                                num +=1
                            }
                            it.num = num
                            lastScore = it.score
                        }

                        trySend(Response.Success(usersScoreWithProfiles))
                    }
                } else {
                    trySend(Response.Failure(Exception("Нет данных по usersScore")))
                }
            }
        awaitClose {
            usersScoreListenerRegistration.remove()
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