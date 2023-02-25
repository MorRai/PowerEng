package com.rai.powereng.ui.authorization.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.usecase.auth.SignInWithEmailPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignInViewModel(private val signInWithEmailPassword: SignInWithEmailPassword
): ViewModel()  {

    private val _signInResponse= MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val signInResponse: StateFlow<Response<Boolean>> = _signInResponse

    fun signUpUser(email: String, password: String) = viewModelScope.launch {
        _signInResponse.value = Response.Loading
        val result = signInWithEmailPassword.invoke( email, password)
        _signInResponse.value = result
    }

}