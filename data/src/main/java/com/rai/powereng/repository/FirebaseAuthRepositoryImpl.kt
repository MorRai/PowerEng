package com.rai.powereng.repository


import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rai.powereng.mapper.toDomainModels
import com.rai.powereng.mapper.toUser
import com.rai.powereng.model.Response
import com.rai.powereng.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent


internal class FirebaseAuthRepositoryImpl(private val auth: FirebaseAuth,
                                         // private var oneTapClient: SignInClient,
                                          private val storage: FirebaseStorage,
                                          private val db: FirebaseFirestore
): FirebaseAuthRepository, KoinComponent {


    private val currentUserFlow = MutableStateFlow<FirebaseUser?>(null)


    init {
        auth.addAuthStateListener { firebaseAuth ->
            currentUserFlow.value = firebaseAuth.currentUser
        }
    }

    override val isUserAuthenticatedInFirebase:Boolean
        get() = currentUserFlow.value?.isEmailVerified ?: false

    override val currentUserId: String
        get() = currentUserFlow.value?.uid.orEmpty()

    override val currentUser: User?
        get() = currentUserFlow.value?.toDomainModels()

    override fun getCurrentUser(viewModelScope: CoroutineScope): StateFlow<User?> {
        return currentUserFlow.map { it?.toDomainModels() }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), currentUserFlow.value?.toDomainModels())
    }


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

    override suspend fun signUpWithEmailPassword(email: String, password: String) : Response<Boolean> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                addUserToFirestore()
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
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
          currentUserFlow.value?.sendEmailVerification()?.await()
          Response.Success(true)
        } catch (e: Exception) {
          Response.Failure(e)
        }

    override suspend fun reloadFirebaseUser()=flow {
        try {
            emit(Response.Loading)
            currentUserFlow.value?.reload()?.await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun revokeAccess()=flow {
        try {
            emit(Response.Loading)
            currentUserFlow.value?.delete()?.await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    private suspend fun addUserToFirestore() {
        currentUserFlow.value?.apply {
            val user = toUser()
            db.collection("users").document(uid).set(user).await()
        }
    }

    override suspend fun updateCurrentUser(email: String, name: String, photoUri: String?): Response<Boolean> {
        val user = currentUserFlow.value ?: return Response.Failure(Exception("User is not authenticated"))

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .apply {
                photoUri?.let { uri ->
                    val photoUrl = uploadImageToFirebaseStorage(uri.toUri()) ?: return Response.Failure(Exception("Failed to upload image"))
                    setPhotoUri(photoUrl)
                }
            }
            .build()

        return try {
            user.updateEmail(email).await()
            user.updateProfile(profileUpdates).await()
            updateUserToFirestore()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    private suspend fun uploadImageToFirebaseStorage(imageUri: Uri): Uri? {
        val imageName =  currentUserFlow.value?.uid
        val imageRef = storage.reference.child("images/$imageName")
        return try {
            val uploadTask = imageRef.putFile(imageUri).await()
            imageRef.downloadUrl.await()
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun updateUserToFirestore() {
        currentUserFlow.value?.apply {
            val user = toUser()
            db.collection("users").document(uid).set(user).await()
        }
    }



//private var signInRequest = get<BeginSignInRequest>(named("signInRequest"))
//private var signUpRequest = get<BeginSignInRequest>(named("signUpRequest"))
    /*
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
}

