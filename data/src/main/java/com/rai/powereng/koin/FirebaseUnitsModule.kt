package com.rai.powereng.koin

import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.qualifier.named
import org.koin.dsl.module

val firebaseUnitsModule = module {
    single(named("unitList")) {
        get<FirebaseFirestore>().collection("unitList")
    }

    single { FirebaseFirestore.getInstance() }//its db

    //FirebaseFirestore.getInstance().collection("unitList")
}