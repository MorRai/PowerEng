package com.rai.powereng.koin

import com.rai.powereng.usecase.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import com.rai.powereng.usecase.auth.*
import com.rai.powereng.usecase.multiplayer.CreateGameUseCase
import com.rai.powereng.usecase.multiplayer.GetScoreGameUseCase
import com.rai.powereng.usecase.multiplayer.JoinGameUseCase
import com.rai.powereng.usecase.multiplayer.SaveGameUseCase

internal val useCaseModule = module {
    factoryOf(::SignInWithEmailPassword)
    factoryOf(::SendPasswordReset)
    factoryOf(::SignOut)
    factoryOf(::SignUpWithEmailPassword)
    factoryOf(::GetCurrentUser)
    factoryOf(::SendEmailVerification)
    factoryOf(::SignInWithGoogle)
    factoryOf(::OneTapSignInWithGoogle)
    factoryOf(::GetUnitsUseCase)
    factoryOf(::GetTasksUseCase)
    factoryOf(::GetAmountTasksInPartUseCase)
    factoryOf(::AddUserInfoUseCase)
    factoryOf(::TransferUserInfoUseCase)
    factoryOf(::GetUserIdUseCase)
    factoryOf(::GetUsersScoreUseCase)
    factoryOf(::GetUserScoreUseCase)
    factoryOf(::JoinGameUseCase)
    factoryOf(::CreateGameUseCase)
    factoryOf(::SaveGameUseCase)
    factoryOf(::GetScoreGameUseCase)

}