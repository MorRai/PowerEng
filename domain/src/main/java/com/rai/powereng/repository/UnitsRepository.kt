package com.rai.powereng.repository

import com.rai.powereng.model.Response
import com.rai.powereng.model.UnitData
import kotlinx.coroutines.flow.Flow

interface UnitsRepository {
    fun getUnitsData(): Flow<Response<List<UnitData>>>
}