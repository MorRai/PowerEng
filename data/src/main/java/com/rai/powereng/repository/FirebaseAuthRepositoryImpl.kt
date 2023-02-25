package com.rai.powereng.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rai.powereng.model.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl : FirebaseAuthRepository{

    //override val currentUser get() = auth.currentUser


    override suspend fun signOut(): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            Firebase.auth.signOut()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }


    override suspend fun signUpWithEmailPassword(email: String, password: String) =
        try {
            Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }


    override suspend fun signInWithEmailPassword(email: String, password: String) =
        try {
            Firebase.auth.signInWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }


    override suspend fun sendPasswordReset(email: String) =
        try {
            Firebase.auth.sendPasswordResetEmail(email).await()
           Response.Success(true)
        } catch (e: Exception) {
          Response.Failure(e)
        }


    override suspend fun sendEmailVerification()=
      try {
          Firebase.auth.currentUser?.sendEmailVerification()?.await()
          Response.Success(true)
        } catch (e: Exception) {
          Response.Failure(e)
        }


    override suspend fun reloadFirebaseUser()=flow {
        try {
            emit(Response.Loading)
            Firebase.auth.currentUser?.reload()?.await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun revokeAccess()=flow {
        try {
            emit(Response.Loading)
            Firebase.auth.currentUser?.delete()?.await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }


    override fun getFirebaseAuthState(viewModelScope: CoroutineScope) = callbackFlow  {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        Firebase.auth.addAuthStateListener(authStateListener)
        awaitClose {
            Firebase.auth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(),   Firebase.auth.currentUser == null)

}