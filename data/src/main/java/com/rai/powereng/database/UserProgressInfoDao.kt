package com.rai.powereng.database

import androidx.room.*
import com.rai.powereng.model.MaxUnitAndPart
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

    @Query("SELECT MAX(part_id) as max_part_id, MAX(unit_id) as max_unit_id FROM user_progress_info WHERE unit_id = (SELECT MAX(unit_id) FROM user_progress_info)")
    suspend fun getMaxUnitAndPart(): MaxUnitAndPart
}