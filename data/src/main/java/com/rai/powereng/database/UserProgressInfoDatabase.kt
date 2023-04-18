package com.rai.powereng.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rai.powereng.model.UserProgressInfoEntity

@Database(entities = [UserProgressInfoEntity::class], version = 1, exportSchema = false)
internal abstract class UserProgressInfoDatabase: RoomDatabase() {
    abstract fun userProgressInfoDao(): UserProgressInfoDao
}