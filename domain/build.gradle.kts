plugins {
    id("org.jetbrains.kotlin.jvm")
    id("java-library")
    id("com.google.gms.google-services")

}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    implementation(libs.kotlinx.coroutine)
}