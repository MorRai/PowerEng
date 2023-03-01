package com.rai.powereng.repository

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.rai.powereng.model.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent



class FirebaseAuthRepositoryImpl(private val auth: FirebaseAuth,
                                 //private var oneTapClient: SignInClient,
                                 private val db: FirebaseFirestore
): FirebaseAuthRepository, KoinComponent {


    override val isUserAuthenticatedInFirebase = auth.currentUser != null

    override val isEmailVerified = auth.currentUser?.isEmailVerified ?: false

    /*
    private var signInRequest = get<BeginSignInRequest>(named("signInRequest"))
    private var signUpRequest = get<BeginSignInRequest>(named("signUpRequest"))

    override suspend fun oneTapSignInWithGoogle(): Response<BeginSignInResult> {
        return try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            Response.Success(signInResult)
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                Response.Success(signUpResult)
            } catch (e: Exception) {
                Response.Failure(e)
            }
        }
    }
    */

    override suspend fun firebaseSignInWithGoogle(idToken: String): Response<Boolean> {
        return try {
            val googleCredential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                addUserToFirestore()
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun signOut() =
        try {
            auth.signOut()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun signUpWithEmailPassword(email: String, password: String) =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun signInWithEmailPassword(email: String, password: String) =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun sendPasswordReset(email: String) =
        try {
            auth.sendPasswordResetEmail(email).await()
           Response.Success(true)
        } catch (e: Exception) {
          Response.Failure(e)
        }

    override suspend fun sendEmailVerification()=
      try {
          auth.currentUser?.sendEmailVerification()?.await()
          Response.Success(true)
        } catch (e: Exception) {
          Response.Failure(e)
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
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(),   auth.currentUser == null)

    private suspend fun addUserToFirestore() {
        auth.currentUser?.apply {
            val user = toUser()
            db.collection("users").document(uid).set(user).await()
        }
    }
}


fun FirebaseUser.toUser() = mapOf(
    "displayName" to displayName,
    "email" to email,
    "photoUrl" to photoUrl?.toString(),
    "createdAt" to serverTimestamp()
)