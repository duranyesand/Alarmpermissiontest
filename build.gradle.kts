// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
//        classpath(libs.oss.licenses.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false

//    id ("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
//    id ("kotlinx-serialization")

    id ("com.google.dagger.hilt.android") version "2.49" apply false

}