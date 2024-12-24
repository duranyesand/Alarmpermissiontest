plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlinx-serialization")

    id ("com.google.dagger.hilt.android")
    id ("kotlin-kapt")
}

android {


    namespace = "com.example.alarmpermissiontest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.alarmpermissiontest"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }


}



dependencies {
    implementation ("androidx.activity:activity-ktx:1.7.1")

    // Media3
    implementation(libs.androidx.media3.player)
    implementation(libs.androidx.media3.player.session)
    implementation(libs.androidx.media3.player.dash)
    implementation(libs.androidx.media3.ui)

    implementation("androidx.media3:media3-exoplayer-hls:1.4.1")

//    // media 재생 관련 UI
//    implementation(libs.androidx.media3.ui)
//    //
//    implementation(libs.androidx.media3.common)
//    // media3 session
//    implementation(libs.androidx.media3.session)

    implementation ("androidx.multidex:multidex:2.0.1")
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.immutable)
    implementation(libs.androidx.datastore)

    // hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
//    implementation (libs.androidx.hilt.compiler)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // serialization
    implementation(libs.kotlinx.serialization.json)

    // coroutine_guava
    implementation(libs.coroutines.guava)

//    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")


}
kapt {
    correctErrorTypes = true
}