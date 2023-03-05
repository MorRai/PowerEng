package com.rai.powereng.repository

import com.google.firebase.firestore.CollectionReference
import com.rai.powereng.model.Response
import com.rai.powereng.model.UnitData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class UnitsRepositoryImpl(private val unitsCollRef: CollectionReference):UnitsRepository {

    override suspend fun getUnitsData(): Flow<Response<List<UnitData>>> = flow {
        try {
            emit(Response.Loading)
            val units = getMoviesFromCloudFirestore()
            emit(Response.Success(units))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    private suspend fun getMoviesFromCloudFirestore(): List<UnitData> {
        return unitsCollRef.orderBy("unitId").get().await().toObjects(UnitData::class.java)
    }


}