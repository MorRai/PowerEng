package com.rai.powereng.koin

import com.rai.powereng.repository.FirebaseAuthRepository
import com.rai.powereng.repository.FirebaseAuthRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val firebaseAuthRepositoryModule = module {
    singleOf(::FirebaseAuthRepositoryImpl){bind<FirebaseAuthRepository>() }
}