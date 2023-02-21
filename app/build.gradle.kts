plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {

    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.rai.powereng"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data"))

    implementation (platform("com.google.firebase:firebase-bom:29.2.1"))
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-storage:20.1.0")
    implementation ("com.google.firebase:firebase-firestore-ktx")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.4.1")
    implementation(libs.bundles.androidx.navigation)
    implementation(libs.kotlinx.coroutine)
    implementation(libs.koin.android)
    implementation(libs.bundles.androidx.lifecycle)

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso)
}