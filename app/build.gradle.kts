plugins {
    id("com.android.application")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

private val projectJvmTarget = JavaVersion.VERSION_11

kotlin {
    jvmToolchain(projectJvmTarget.majorVersion.toInt())
}

android {
    compileOptions {
        sourceCompatibility = projectJvmTarget
        targetCompatibility = projectJvmTarget
    }

    namespace = "com.ataulm.whatsnext"
    compileSdk = 34
    android.buildFeatures.buildConfig = true
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }


    defaultConfig {
        applicationId = "com.ataulm.whatsnext"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    kapt(libs.androidx.lifecycle.compiler)
    kapt(libs.dagger.compiler)

    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)

    implementation(project(":core"))
    implementation(project(":letterboxd"))
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.security.crypto)
    implementation(libs.coil)
    implementation(libs.dagger)
    implementation(libs.gson)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.stdlib)
    implementation(libs.material)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.timber)

    testImplementation(libs.assertj.core)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.okhttp)
}
