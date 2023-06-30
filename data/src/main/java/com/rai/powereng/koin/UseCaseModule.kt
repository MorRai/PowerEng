package com.rai.powereng.koin

import com.rai.powereng.usecase.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import com.rai.powereng.usecase.auth.*
import com.rai.powereng.usecase.multiplayer.*

internal val useCaseModule = module {
    factoryOf(::SignInWithEmailPassword)
    factoryOf(::SendPasswordReset)
    factoryOf(::SignOut)
    factoryOf(::SignUpWithEmailPassword)
    factoryOf(::GetCurrentUser)
    factoryOf(::SendEmailVerification)
    factoryOf(::SignInWithGoogle)
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
    factoryOf(::GenerateGameCodeUseCase)
    factoryOf(::RefreshUserScoreUseCase)
    factoryOf(::GetAllTasksOfUnitUseCase)
}