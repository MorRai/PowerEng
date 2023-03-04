package com.rai.powereng.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.repository.FirebaseAuthRepository
import com.rai.powereng.usecase.auth.GetAuthState

class AuthViewModel(private val getAuthState: GetAuthState,
                     firebaseAuthRepository: FirebaseAuthRepository
): ViewModel() {

    init {
        getAuthStateResponse()
    }

    fun getAuthStateResponse() =  getAuthState.invoke(viewModelScope)

    val isEmailVerified  =   firebaseAuthRepository.isEmailVerified

}