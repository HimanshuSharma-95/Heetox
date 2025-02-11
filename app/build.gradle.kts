plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")

}

android {
    namespace = "com.heetox.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.heetox.app"
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.protolite.well.known.types)
    implementation(libs.androidx.media3.transformer)
    implementation(libs.androidx.camera.lifecycle)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //compose
    val material_version = "1.3.1"
//        implementation ("androidx.compose.material3:material3:$material_version")
        implementation ("androidx.compose.material:material-icons-extended:$material_version")
        implementation ("androidx.compose.ui:ui-tooling-preview:$material_version")

    implementation ("androidx.compose.ui:ui:1.3.0") // Or the latest version
    implementation ("androidx.compose.material3:material3:1.0.0-alpha13") // Or the latest version
    implementation ("androidx.compose.foundation:foundation:1.3.0") // Or the latest version



    //splash
    implementation("androidx.core:core-splashscreen:1.0.1")


    //Retrofit
    val retrofit_version = "2.11.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation ("com.squareup.retrofit2:converter-gson:$retrofit_version")


    //Coroutines
    val coroutines_version = "1.9.0-RC"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")


    //lifecycle
    val lifecycle_version = "2.8.3"
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")


    //nav
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")


    //naviagtion animation
    implementation ("androidx.compose.animation:animation:1.2.0")
    implementation("androidx.compose.animation:animation-graphics:1.2.0")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.30.1")



    //hilt
        val hiltVersion = "2.51"
        implementation("com.google.dagger:hilt-android:$hiltVersion")
        ksp("com.google.dagger:hilt-android-compiler:$hiltVersion")
        ksp("com.google.dagger:hilt-compiler:$hiltVersion")


    //encrypted sharedprefrence
    implementation ("androidx.security:security-crypto:1.1.0-alpha06")


    //coil image loading
    implementation("io.coil-kt:coil-compose:2.7.0")
    

    //flow row
    implementation ("com.google.accompanist:accompanist-flowlayout:0.28.0")



    //barcode + Camera
    implementation ("com.google.mlkit:barcode-scanning:17.2.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.0")
    implementation ("androidx.activity:activity-compose:1.4.0")

    implementation ("androidx.camera:camera-camera2:1.0.0")
    implementation ("androidx.camera:camera-lifecycle:1.0.0")
    implementation ("androidx.camera:camera-view:1.0.0-alpha21")



    // Paging
    // Paging Compose Integration
    implementation ("androidx.paging:paging-compose:1.0.0-alpha17")


    //swipe refresh
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.24.13-rc")


    //bars
//   Charts Library
    implementation("io.github.bytebeats:compose-charts:0.2.1")



}