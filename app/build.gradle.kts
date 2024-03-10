import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled


plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")

}

android {
    namespace = "com.example.moviesapp"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }


    defaultConfig {
        applicationId = "com.example.moviesapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation (libs.retrofit2.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.livedata)
    implementation (libs.androidx.lifecycle.viewmodel.ktx.v231)
    implementation(libs.androidx.room.runtime)
    var room_version = ("2.6.1")

    implementation (libs.androidx.room.runtime.v240)
    annotationProcessor (libs.androidx.room.compiler)
    //noinspection KaptUsageInsteadOfKsp
    kapt (libs.androidx.room.compiler)

    implementation(libs.kotlinx.coroutines.android)
    implementation (libs.squareup.picasso)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.room.ktx)



}