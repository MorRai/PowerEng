package com.rai.powereng.ui.authorization.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.rai.powereng.model.Response
import com.rai.powereng.usecase.auth.SignInWithEmailPassword
import com.rai.powereng.usecase.auth.SignInWithGoogle
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignInViewModel(private val signInWithEmailPassword: SignInWithEmailPassword,

                      private val signInWithGoogle: SignInWithGoogle,
                      val oneTapClient: SignInClient,
                      private var signInRequest:BeginSignInRequest,
                      private var signUpRequest:BeginSignInRequest,
    // private val oneTapSignInWithGoogle: OneTapSignInWithGoogle
): ViewModel() {



    private val _signInResponse= MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val signInResponse: StateFlow<Response<Boolean>> = _signInResponse

    private val _oneTapSignInResponse= MutableStateFlow<Response<BeginSignInResult?>>(Response.Success(null))
    val oneTapSignInResponse: StateFlow<Response<BeginSignInResult?>> = _oneTapSignInResponse

    private val _signInWithGoogleResponse = MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val signInWithGoogleResponse : StateFlow<Response<Boolean>> = _signInWithGoogleResponse

    fun signUpUser(email: String, password: String) = viewModelScope.launch {
        _signInResponse.value = Response.Loading
        val result = signInWithEmailPassword.invoke( email, password)
        _signInResponse.value = result
    }

    fun oneTapSignIn() = viewModelScope.launch {
        _oneTapSignInResponse.value = Response.Loading
       // val result = oneTapSignInWithGoogle.invoke()
        val result = oneTapSignInWithGoogle()
        _oneTapSignInResponse.value = result
    }

    fun signInWithGoogle(idToken:String) = viewModelScope.launch {
        _signInWithGoogleResponse.value = Response.Loading
        val result = signInWithGoogle.invoke(idToken)
        _signInWithGoogleResponse.value = result
    }

    private suspend fun oneTapSignInWithGoogle(): Response<BeginSignInResult> {
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

}