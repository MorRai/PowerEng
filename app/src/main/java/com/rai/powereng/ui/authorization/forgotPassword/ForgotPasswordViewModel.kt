package com.rai.powereng.ui.authorization.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.usecase.auth.SendPasswordReset
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ForgotPasswordViewModel(sendPasswordReset: SendPasswordReset,
                              email:String): ViewModel() {

    val sendPasswordResetEmailResponse  = sendPasswordReset.invoke(email).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = Response.Loading
    )

}

