package com.rai.powereng.model

import androidx.room.ColumnInfo

data class MaxUnitAndPart(
    @ColumnInfo(name = "max_unit_id") val maxUnitId: Int = 1,
    @ColumnInfo(name = "max_part_id") val maxPartId: Int = 0
)