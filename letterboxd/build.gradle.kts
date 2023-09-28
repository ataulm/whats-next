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
    namespace = "com.ataulm.letterboxd"
    compileSdk = libs.versions.sdk.compile.get().toInt()
    buildFeatures.buildConfig = true
    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
        buildConfigField(
            "String",
            "LETTERBOXD_KEY",
            "\"${project.properties["letterboxd_api_key"] as String}\""
        )

        buildConfigField(
            "String",
            "LETTERBOXD_SECRET",
            "\"${project.properties["letterboxd_api_secret"] as String}\""
        )
    }
}

dependencies {
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)

    implementation(project(":domain"))
    implementation(libs.androidx.security.crypto)
    implementation(libs.dagger)
    implementation(libs.hilt.android)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)

    kapt(libs.dagger.compiler)
    kapt(libs.hilt.compiler)

    ksp(libs.moshi)
}
