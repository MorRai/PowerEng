package com.rai.powereng.koin

import org.koin.dsl.module

val dataModule = module {
    includes(
        firebaseAuthRepositoryModule,
        firebaseRequestModule,
        unitsRepositoryModule,
        tasksRepositoryModule,
        userProgressInfoRepositoryModule,
        useCaseModule,
        userProgressInfoDatabaseModule
    )
}