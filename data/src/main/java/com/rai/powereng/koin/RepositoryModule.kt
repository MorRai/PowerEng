package com.rai.powereng.koin

import com.rai.powereng.repository.FirebaseAuthRepository
import com.rai.powereng.repository.FirebaseAuthRepositoryImpl
import com.rai.powereng.repository.UnitsRepositoryImpl
import com.rai.powereng.repository.UnitsRepository
import com.rai.powereng.repository.TasksRepositoryImpl
import com.rai.powereng.repository.TasksRepository
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