import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android) apply false
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

//    val compileKotlin: KotlinCompile by tasks
//
//    compileKotlin.kotlinOptions {
//        jvmTarget = JavaVersion.VERSION_17.toString()
//    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
//    kotlinOptions {
//        jvmTarget = "17"
//    }

}

dependencies {

    // Media3
    // media 재생 관련 UI
    implementation("androidx.media3:media3-ui:1.4.1")
    //
    implementation("androidx.media3:media3-common:1.4.1")
    // media3 session
    implementation("androidx.media3:media3-session:1.4.1")


    // hilt
    implementation("com.google.dagger:hilt-android:2.49")
    implementation(libs.androidx.media3.exoplayer)
    implementation (libs.androidx.hilt.compiler)
    implementation(libs.hilt.android.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")22

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

}
kapt {
    correctErrorTypes = true
}