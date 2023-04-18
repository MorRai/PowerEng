package com.rai.powereng.koin

import androidx.room.Room
import com.rai.powereng.database.UserProgressInfoDatabase
import org.koin.dsl.module


internal val userProgressInfoDatabaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            UserProgressInfoDatabase::class.java,
            "user_progress_info_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}