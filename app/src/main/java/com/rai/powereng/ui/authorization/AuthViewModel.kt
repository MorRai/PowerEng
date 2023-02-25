package com.rai.powereng.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rai.powereng.usecase.auth.GetAuthState

class AuthViewModel(getAuthState: GetAuthState,
): ViewModel() {

    val authState = getAuthState.invoke(viewModelScope)

    val isEmailVerified get() =   Firebase.auth.currentUser?.isEmailVerified ?: false

}