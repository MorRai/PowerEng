package com.rai.powereng.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress_info")
internal data class UserProgressInfoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "points") val points: Int,
    @ColumnInfo(name = "data") val data: String,
    @ColumnInfo(name = "mistakes") val mistakes: Int,
    @ColumnInfo(name = "unit_id") val unitId: Int,
    @ColumnInfo(name = "part_id") val partId: Int,
)


