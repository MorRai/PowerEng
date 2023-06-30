package com.rai.powereng.ui.authorization.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.usecase.auth.SendEmailVerification
import com.rai.powereng.usecase.auth.SignUpWithEmailPassword
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpWithEmailPassword: SignUpWithEmailPassword,
    private val sendEmailVerification: SendEmailVerification,
) : ViewModel() {

    private val _signUpUserFlow = MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val signUpUserFlow: StateFlow<Response<Boolean>> = _signUpUserFlow

    private val _sendEmailVerificationFlow =
        MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val sendEmailVerificationFlow: StateFlow<Response<Boolean>> = _sendEmailVerificationFlow

    fun signUpUser(email: String, password: String) = viewModelScope.launch {
        _signUpUserFlow.value = Response.Loading
        val result = signUpWithEmailPassword.invoke(email, password)
        _signUpUserFlow.value = result
    }

    fun sendEmailVerification() = viewModelScope.launch {
        _sendEmailVerificationFlow.value = Response.Loading
        _sendEmailVerificationFlow.value = sendEmailVerification.invoke()
    }

}