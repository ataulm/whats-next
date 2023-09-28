@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    kotlin("kapt")
}

private val projectJvmTarget = JavaVersion.VERSION_11
kotlin {
    jvmToolchain(projectJvmTarget.majorVersion.toInt())
}
android {
    namespace = "com.ataulm.whatsnext.splash"
    compileSdk = libs.versions.sdk.compile.get().toInt()
    buildFeatures.buildConfig = true
    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    ksp(libs.moshi)
}
