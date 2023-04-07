package com.rai.powereng.koin

import com.rai.powereng.repository.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val firebaseAuthRepositoryModule = module {
    singleOf(::FirebaseAuthRepositoryImpl){bind<FirebaseAuthRepository>() }
}

internal val unitsRepositoryModule = module {
    singleOf(::UnitsRepositoryImpl){bind<UnitsRepository>() }
}

internal val tasksRepositoryModule = module {
    singleOf(::TasksRepositoryImpl){bind<TasksRepository>() }
}

internal val userProgressInfoRepositoryModule = module {
    singleOf(::UserProgressInfoRepositoryImpl){bind<UserProgressInfoRepository>() }
}