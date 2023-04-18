package com.rai.powereng.repository

import com.rai.powereng.model.Response
import com.rai.powereng.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


interface FirebaseAuthRepository {

    val isUserAuthenticatedInFirebase: Boolean

    val currentUserId: String

     fun getCurrentUser(viewModelScope: CoroutineScope): StateFlow<User?>

    //suspend fun oneTapSignInWithGoogle(): Response<BeginSignInResult?>
    suspend fun updateCurrentUser(email: String, name: String, photoUri: String?): Response<Boolean>
    suspend fun firebaseSignInWithGoogle(idToken: String): Response<Boolean>

    suspend fun signOut(): Response<Boolean>

    suspend fun signUpWithEmailPassword(email: String, password: String):Response<Boolean>

    suspend fun signInWithEmailPassword(email: String, password: String):Response<Boolean>

    suspend fun sendPasswordReset(email: String):Response<Boolean>

    suspend fun sendEmailVerification():Response<Boolean>

    suspend fun reloadFirebaseUser():Flow<Response<Boolean>>

    suspend fun revokeAccess():Flow<Response<Boolean>>
}