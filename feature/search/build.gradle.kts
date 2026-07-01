@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

private val projectJvmTarget = JavaVersion.VERSION_11
kotlin {
    jvmToolchain(projectJvmTarget.majorVersion.toInt())
}
android {
    namespace = "com.ataulm.whatsnext.search"
    compileSdk = libs.versions.sdk.compile.get().toInt()
    buildFeatures.buildConfig = true
    buildFeatures {
        compose = true
    }
    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":letterboxd"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
}
