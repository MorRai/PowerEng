package com.rai.powereng.repository

import com.google.firebase.auth.FirebaseAuth
import com.rai.powereng.model.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl( private val auth: FirebaseAuth) : FirebaseAuthRepository{

    override val currentUser get() = auth.currentUser


    override suspend fun signOut(): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            auth.signOut()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }


    override suspend fun signUpWithEmailPassword(name: String, email: String, password: String) = flow  {
        try {
            emit(Response.Loading)
            auth.createUserWithEmailAndPassword(email, password).await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun signInWithEmailPassword(email: String, password: String) = flow  {
        try {
            emit(Response.Loading)
            auth.signInWithEmailAndPassword(email, password).await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun sendPasswordReset(email: String) = flow {
        try {
            emit(Response.Loading)
            auth.sendPasswordResetEmail(email).await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun sendEmailVerification()=flow {
      try {
            emit(Response.Loading)
            auth.currentUser?.sendEmailVerification()?.await()
          emit(Response.Success(true))
        } catch (e: Exception) {
          emit(Response.Failure(e))
        }
    }

    override suspend fun reloadFirebaseUser()=flow {
        try {
            emit(Response.Loading)
            auth.currentUser?.reload()?.await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun revokeAccess()=flow {
        try {
            emit(Response.Loading)
            auth.currentUser?.delete()?.await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }


    override fun getFirebaseAuthState(viewModelScope: CoroutineScope) = callbackFlow  {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), auth.currentUser == null)


}