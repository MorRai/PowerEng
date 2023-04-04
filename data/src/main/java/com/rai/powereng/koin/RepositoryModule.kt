package com.rai.powereng.koin

import com.rai.powereng.repository.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val firebaseAuthRepositoryModule = module {
    singleOf(::FirebaseAuthRepositoryImpl){bind<FirebaseAuthRepository>() }
}

val unitsRepositoryModule = module {
    singleOf(::UnitsRepositoryImpl){bind<UnitsRepository>() }
}

val tasksRepositoryModule = module {
    singleOf(::TasksRepositoryImpl){bind<TasksRepository>() }
}

val userProgressInfoRepositoryModule = module {
    singleOf(::UserProgressInfoRepositoryImpl){bind<UserProgressInfoRepository>() }
}