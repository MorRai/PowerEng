package com.rai.powereng.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.rai.powereng.model.Response
import com.rai.powereng.model.UnitData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class UnitsRepositoryImpl(
    private val db: FirebaseFirestore
    ):UnitsRepository {

    override fun getUnitsData() = callbackFlow {
        val snapshotListener = db.collection("unitList").addSnapshotListener { snapshot, e ->
            val unitsResponse = if (snapshot != null) {
                val units = snapshot.toObjects(UnitData::class.java)
                Response.Success(units)
            } else {
                Response.Failure(e ?:  Exception("Unknown error"))
            }
            trySend(unitsResponse)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

}

