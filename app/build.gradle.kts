plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
//    kotlin("kapt")
//    kotlin("android")
    id ("kotlin-kapt")
    id ("org.jetbrains.kotlin.android")
}

repositories {
    google()
    mavenCentral()
}

android {
    compileSdk = Config.compileSdk
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = Config.packageName
        minSdk = 26
        targetSdk = 30
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf(
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi",
            "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-Xopt-in=com.google.accompanist.pager.ExperimentalPagerApi"
        )
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.version
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.4.0-beta01")
    implementation("com.google.android.material:material:1.4.0")
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.tooling)
    implementation("androidx.activity:activity-compose:1.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation(Dependencies.Compose.junit)

    // ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-rc01")

    // Lifecycle
    val lifecycle_version = "2.4.0-rc01"
//    implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
//    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation(Dependencies.Compose.lifecycle)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")


    // Hilt
    implementation(Dependencies.Hilt.hiltAndroid)
    kapt(Dependencies.Hilt.compiler)
    implementation(Dependencies.Hilt.navigationCompose)

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2-native-mt")

    // Room
    implementation(Dependencies.Room.roomKtx)
    kapt(Dependencies.Room.roomCompiler)

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Material icons
    implementation(Dependencies.Compose.icons)

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha10")

    // Accompanist
    implementation(Dependencies.Compose.insets)
    implementation(Dependencies.Compose.insetsUi)
    implementation(Dependencies.Compose.pager)

    // Preference
    implementation("androidx.preference:preference-ktx:1.1.1")
}