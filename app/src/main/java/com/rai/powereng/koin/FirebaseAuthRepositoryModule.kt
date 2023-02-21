package com.rai.powereng.koin

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.rai.powereng.FirebaseAuthRepository

 val firebaseAuthRepositoryModule = module {
    singleOf(::FirebaseAuthRepository)
}