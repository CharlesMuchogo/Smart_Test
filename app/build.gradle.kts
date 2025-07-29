import java.util.Properties



plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinX.serialization.plugin)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.charlesmuchogo.research"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.charlesmuchogo.research"
        minSdk = 24
        targetSdk = 36
        versionCode = 20
        versionName = "1.4.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material.icons.extended)
    implementation(libs.firebase.messaging)
    implementation(libs.navigation.compose)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.ai)
    implementation(libs.firebase.analytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.play.services.location)
    implementation(libs.play.services.auth)


    //datetime
    implementation(libs.kotlinX.dateTime)

    // Dagger hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.encoding)
    implementation(libs.ktor.client.okhttp)

    // Room Components
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // coil
    implementation(libs.coil.core)
    implementation(libs.coil.compose)

    //image cropper
    implementation(libs.ucrop.compose)

    //exoplayer
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    //Browser
    implementation(libs.inappbrowser)

    //lottie
    implementation(libs.lottie)

    //work manager
    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.slf4j.nop)

    implementation(libs.multiplatformSettings.noArg)
    implementation(libs.multiplatformSettings.coroutines)

    implementation(libs.app.update)
    implementation(libs.app.update.ktx)

    // shimmer
    implementation(libs.shimmer.compose)

    implementation(libs.play.services.ads)
}
