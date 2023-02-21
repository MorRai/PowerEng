package com.rai.powereng

import android.app.Application
import com.rai.powereng.koin.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class PowerEngApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
           androidContext(this@PowerEngApplication)
           modules(
               viewModelsModule,
               firebaseAuthRepositoryModule
           )
       }
    }
}