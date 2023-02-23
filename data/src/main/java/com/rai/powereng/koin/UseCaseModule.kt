package com.rai.powereng.koin

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import com.rai.powereng.usecase.auth.*

val useCaseModule = module {
    factoryOf(::SignInWithEmailPassword)
    factoryOf(::SendPasswordReset)
    factoryOf(::SignOut)
    factoryOf(::SignUpWithEmailPassword)
}