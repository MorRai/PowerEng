buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.10")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id ("com.google.gms.google-services") version "4.3.10" apply false
    id("com.android.application") version "7.4.2" apply false
    id("com.android.library") version "7.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
    id("org.jetbrains.kotlin.jvm") version "1.6.21" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.4.1" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}