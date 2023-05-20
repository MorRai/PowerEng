package com.rai.powereng.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rai.powereng.model.Response
import com.rai.powereng.model.UserMultiplayer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

internal class UsersMultiplayerRepositoryImpl : UsersMultiplayerRepository {

    private val databaseReference =
        FirebaseDatabase.getInstance("https://powereng-cac3c-default-rtdb.europe-west1.firebasedatabase.app/").reference

    override suspend fun generateGameCode(postfix:String): Response.Success<String> {
        val charPool : List<Char> = ('0'..'9') + ('a'..'z') + ('A'..'Z')
        val gameCode = (1..6).map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
        val gameSnapshot = databaseReference.child("games").child(gameCode + postfix).get().await() // check if it exists in the database
        return if (gameSnapshot.exists()) {
            generateGameCode(postfix) // if the code is already taken, generate a new one
        } else {
           Response.Success(gameCode)  // otherwise return the generated code
        }
    }

    override suspend fun createGame(gameCode: String, playerName: String,playerImage:String) : Response<Boolean> {
        return try {
            val gameRef = databaseReference.child("games").child(gameCode)
            // Check if there is a game with this gameCode
            val gameSnapshot = gameRef.get().await()
            if (gameSnapshot.exists()) {
                return Response.Failure(Exception("Game with this code already exists"))
            }
            val userUserRef =
                databaseReference.child("games").child(gameCode).child("answers").child(playerName)
            val answers = hashMapOf(
                "name" to playerName,
                "score" to 0,
                "time" to 0,
                "isComplete" to false,
                "isForDelete" to false,
                "image" to playerImage
            )
            userUserRef.setValue(answers).await()
            return Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override fun waitForPlayersToJoin(gameCode: String): Flow<Response<Boolean>> = callbackFlow {
        val answersRef = databaseReference.child("games").child(gameCode).child("answers")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val players = snapshot.value as HashMap<String, Any>
                    if (players.size == 2) {
                        trySend(Response.Success(true))
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Response.Failure(error.toException())
            }
        }
        answersRef.addValueEventListener(valueEventListener)
        awaitClose { answersRef.removeEventListener(valueEventListener) }
    }

    override suspend fun cancelGame(gameCode: String): Response<Boolean> {
        return try {
            val userAnswersRef = databaseReference.child("games").child(gameCode).child("answers")
            val snapshot = userAnswersRef.get().await()
            val users = snapshot.children.mapNotNull { it.getValue(UserMultiplayer::class.java) }
            when {
                users.size == 1 -> {
                    userAnswersRef.removeValue().await()
                    Response.Success(true)
                }
                users.all { it.isForDelete } -> {
                    userAnswersRef.removeValue().await()
                    Response.Success(true)
                }
                else -> {
                    Response.Failure(Exception("Not all users completed the game"))
                }
            }
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }


    override suspend fun joinGame(gameCode: String, playerName: String,playerImage:String): Response<Boolean>  {
        return try {
            val snapshot = databaseReference.child("games").child(gameCode).child("answers").get().await()
            if (snapshot.exists()) {
                val result = snapshot.value as HashMap<String, Any>
                val numKeys = result.size
                if (numKeys == 1) {
                    val userAnswersRef = databaseReference.child("games").child(gameCode).child("answers").child(playerName)
                    val answers = hashMapOf(
                        "name" to playerName,
                        "score" to 0,
                        "time" to 0,
                        "isComplete" to false,
                        "isForDelete" to false,
                        "image" to playerImage
                    )
                    userAnswersRef.setValue(answers).await()
                    Response.Success(true)
                } else {
                    Response.Failure(Exception("Game code is taken!"))
                }
            } else {
                Response.Failure(Exception("Invalid game code"))
            }
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun saveAnswers(
        numCorrectAnswers: Int,
        gameCode: String,
        playerName: String,
        playerImage:String,
        startTime: Long,
        isComplete:Boolean,
        isForDelete: Boolean
    ) {
        val answersRef = databaseReference.child("games").child(gameCode).child("answers")
        val userAnswersRef = answersRef.child(playerName)
        val elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000
        val answers = hashMapOf(
            "name" to playerName,
            "score" to numCorrectAnswers,
            "time" to elapsedSeconds,
            "isComplete" to isComplete,
            "isForDelete" to isForDelete,
            "image" to playerImage
        )
        userAnswersRef.setValue(answers).await()
        if (isForDelete){
            cancelGame(gameCode)
        }
    }

    override fun showAnswers(gameCode: String): Flow<Response<List<UserMultiplayer>>> = callbackFlow {
        val answersRef = databaseReference.child("games").child(gameCode).child("answers")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val players = snapshot.children.mapNotNull { it.key }.toList()
                    val player1Name = players.getOrNull(0)
                    val player2Name = players.getOrNull(1)
                    val userAnswers = mutableListOf<UserMultiplayer>()
                    player1Name?.let { playerName ->
                        snapshot.child(playerName).getValue(UserMultiplayer::class.java)?.let {
                            userAnswers.add(it)
                        }
                    }
                    player2Name?.let { playerName ->
                        snapshot.child(playerName).getValue(UserMultiplayer::class.java)?.let {
                            userAnswers.add(it)
                        }
                    }
                    try {
                        trySend(Response.Success(userAnswers))
                    } catch (ex: Exception) {
                        Response.Failure(ex)
                    }
                } else {
                    try {
                        trySend(Response.Success(emptyList()))
                    } catch (ex: Exception) {
                        Response.Failure(ex)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Response.Failure(error.toException())
                try {
                    trySend(Response.Success(emptyList()))
                } catch (ex: Exception) {
                    Response.Failure(ex)
                }
            }
        }
        answersRef.addValueEventListener(valueEventListener)
        awaitClose { answersRef.removeEventListener(valueEventListener) }
    }
}