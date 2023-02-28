package com.rai.powereng.repository

import com.rai.powereng.model.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


interface FirebaseAuthRepository {

    val isUserAuthenticatedInFirebase: Boolean

    suspend fun oneTapSignInWithGoogle(): Response<BeginSignInResult?>

    suspend fun firebaseSignInWithGoogle(idToken: String): Response<Boolean>

    suspend fun signOut(): Response<Boolean>

    fun getFirebaseAuthState(viewModelScope: CoroutineScope): StateFlow<Boolean>

    suspend fun signUpWithEmailPassword(email: String, password: String):Response<Boolean>

    suspend fun signInWithEmailPassword(email: String, password: String):Response<Boolean>

    suspend fun sendPasswordReset(email: String):Response<Boolean>

    suspend fun sendEmailVerification():Response<Boolean>



    suspend fun reloadFirebaseUser():Flow<Response<Boolean>>

    suspend fun revokeAccess():Flow<Response<Boolean>>
}