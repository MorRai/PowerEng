package com.rai.powereng.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rai.powereng.model.Response
import com.rai.powereng.model.UnitData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

internal class UnitsRepositoryImpl(
    private val db: FirebaseFirestore,
) : UnitsRepository {

    override fun getUnitsData() = callbackFlow {
        val snapshotListener = db.collection("unitList").addSnapshotListener { snapshot, e ->
            val unitsResponse = if (snapshot != null) {
                val units = snapshot.toObjects(UnitData::class.java)
                units.sortBy { it.unitId }
                Response.Success(units)
            } else {
                Response.Failure(e ?: Exception("Unknown error"))
            }
            trySend(unitsResponse)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

}

