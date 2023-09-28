@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    kotlin("kapt")
    alias(libs.plugins.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
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
    compileSdk = libs.versions.sdk.compile.get().toInt()
    android.buildFeatures.buildConfig = true
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtension.get()
    }


    defaultConfig {
        applicationId = "com.ataulm.whatsnext"
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()
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
    kapt(libs.hilt.compiler)

    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)
    implementation(project(":domain"))
    implementation(project(":letterboxd"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.recyclerview)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.dagger)
    implementation(libs.gson)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
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
