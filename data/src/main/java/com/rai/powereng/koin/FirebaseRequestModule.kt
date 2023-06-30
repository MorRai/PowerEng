package com.rai.powereng.koin

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.rai.powereng.BuildConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val firebaseRequestModule = module {
    single(named("signInRequest")) {
        BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                .setServerClientId(BuildConfig.GOOGLE_FIREBASE_KEY)
                .setFilterByAuthorizedAccounts(true).build()
        ).setAutoSelectEnabled(true).build()
    }


    single(named("signUpRequest")) {
        BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                .setServerClientId(BuildConfig.GOOGLE_FIREBASE_KEY)
                .setFilterByAuthorizedAccounts(false).build()
        ).build()
    }

    single { Firebase.auth }//FirebaseAuth

    single { Firebase.storage }

    single { Firebase.firestore }//FirebaseFirestore

    single { Identity.getSignInClient(androidContext()) } //SignInClient

    single {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_FIREBASE_KEY).requestEmail().build()
    }

    single {
        GoogleSignIn.getClient(androidApplication(), get())
    }
}