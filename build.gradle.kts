// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false
    alias(libs.plugins.android.ksp) apply false

}
