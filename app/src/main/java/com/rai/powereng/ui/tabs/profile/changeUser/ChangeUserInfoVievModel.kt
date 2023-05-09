package com.rai.powereng.ui.tabs.profile.changeUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.repository.FirebaseAuthRepository
import com.rai.powereng.usecase.auth.GetCurrentUser
import com.rai.powereng.usecase.auth.SignOut
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ChangeUserInfoVievModel(
    private val signOut: SignOut,
    private val getCurrentUser: GetCurrentUser,
    private val authRepository: FirebaseAuthRepository,
) : ViewModel() {

    init {
        getCurrentUserResponse()
    }

    private val _signOutResponse = MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val signOutResponse: StateFlow<Response<Boolean>> = _signOutResponse

    private val _updateUserResponse = MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val updateUserResponse: StateFlow<Response<Boolean>> = _updateUserResponse

    fun getCurrentUserResponse() = getCurrentUser.invoke(viewModelScope)

    fun signOutUser() = viewModelScope.launch {
        _signOutResponse.value = Response.Loading
        val result = signOut.invoke()
        _signOutResponse.value = result
    }

    fun updateUser(email:String,name:String, photoUri:String?) = viewModelScope.launch {
        _updateUserResponse.value = Response.Loading
        val result = authRepository.updateCurrentUser(email,name, photoUri)
        _updateUserResponse.value = result
    }

}