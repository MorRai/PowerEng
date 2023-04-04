package com.rai.powereng.koin

import com.rai.powereng.usecase.AddUserInfoUseCase
import com.rai.powereng.usecase.GetAmountTasksInPartUseCase
import com.rai.powereng.usecase.GetTasksUseCase
import com.rai.powereng.usecase.GetUnitsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import com.rai.powereng.usecase.auth.*

val useCaseModule = module {
    factoryOf(::SignInWithEmailPassword)
    factoryOf(::SendPasswordReset)
    factoryOf(::SignOut)
    factoryOf(::SignUpWithEmailPassword)
    factoryOf(::GetAuthState)
    factoryOf(::SendEmailVerification)
    factoryOf(::SignInWithGoogle)
    factoryOf(::OneTapSignInWithGoogle)
    factoryOf(::GetUnitsUseCase)
    factoryOf(::GetTasksUseCase)
    factoryOf(::GetAmountTasksInPartUseCase)
    factoryOf(::AddUserInfoUseCase)
}