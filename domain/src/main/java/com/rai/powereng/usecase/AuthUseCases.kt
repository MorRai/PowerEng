package com.rai.powereng.usecase

import com.rai.powereng.usecase.auth.*

data class AuthUseCases (
    val getAuthState: GetAuthState,
    val signInWithEmailPassword: SignInWithEmailPassword,
    val signOut: SignOut,
    val signUpWithEmailPassword: SignUpWithEmailPassword,
    val sendPasswordReset: SendPasswordReset,
    )