plugins {
    id("org.jetbrains.kotlin.jvm")
    id("java-library")
    id("com.google.gms.google-services")

}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(libs.kotlinx.coroutine)

    implementation (platform("com.google.firebase:firebase-bom:29.2.1"))
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-firestore-ktx")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.4.1")
    implementation("com.google.android.gms:play-services-auth:+")
}