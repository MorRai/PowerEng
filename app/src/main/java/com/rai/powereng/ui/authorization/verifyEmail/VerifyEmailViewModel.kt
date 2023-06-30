package com.rai.powereng.ui.authorization.verifyEmail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.usecase.auth.SignOut
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VerifyEmailViewModel(private val signOut: SignOut) : ViewModel() {

    private val _signOutResponse = MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val signOutResponse: StateFlow<Response<Boolean>> = _signOutResponse

    fun signOutUser() = viewModelScope.launch {
        _signOutResponse.value = Response.Loading
        val result = signOut.invoke()
        _signOutResponse.value = result
    }

}