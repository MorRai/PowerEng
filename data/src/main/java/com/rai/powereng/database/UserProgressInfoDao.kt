package com.rai.powereng.database

import androidx.room.*
import com.rai.powereng.model.UserProgressInfoEntity

@Dao
internal interface UserProgressInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(classPerson: UserProgressInfoEntity)

    @Update
    suspend fun update(classPerson: UserProgressInfoEntity)

    @Delete
    suspend fun delete(classPerson: UserProgressInfoEntity)

    @Query("DELETE FROM user_progress_info")
    suspend fun deleteAll()

    @Query("SELECT * from user_progress_info")
    suspend fun getAll(): List<UserProgressInfoEntity>

}