package com.rai.powereng.ui.authorization.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.usecase.auth.SignInWithEmailPassword
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class SignInViewModel(signInWithEmailPassword: SignInWithEmailPassword,
                      email:String,
                      password: String
): ViewModel()  {

    val signInResponse = signInWithEmailPassword.invoke(email,password).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = Response.Loading
    )
}