package com.rai.powereng.ui.authorization.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.usecase.auth.SendPasswordReset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(private val sendPasswordReset: SendPasswordReset) : ViewModel() {

    private val _sendPasswordResetEmailResponse =
        MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val sendPasswordResetEmailResponse: StateFlow<Response<Boolean>> =
        _sendPasswordResetEmailResponse

    fun signUpUser(email: String) = viewModelScope.launch {
        _sendPasswordResetEmailResponse.value = Response.Loading
        val result = sendPasswordReset.invoke(email)
        _sendPasswordResetEmailResponse.value = result
    }

}

