package com.rai.powereng.koin

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.rai.powereng.ui.authorization.AuthViewModel
import com.rai.powereng.ui.authorization.forgotPassword.ForgotPasswordViewModel
import com.rai.powereng.ui.authorization.signIn.SignInViewModel
import com.rai.powereng.ui.authorization.signUp.SignUpViewModel

val viewModelsModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::ForgotPasswordViewModel)
}