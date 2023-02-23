package com.rai.powereng.ui.authorization.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.usecase.auth.SignUpWithEmailPassword
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class SignUpViewModel(signUpWithEmailPassword: SignUpWithEmailPassword,
                      email:String,
                      password: String): ViewModel()  {

    val signUpResponse = signUpWithEmailPassword.invoke(email,password).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = Response.Loading
    )
}