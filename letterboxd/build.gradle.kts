@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
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
    implementation(project(":core"))
    implementation(libs.moshi.kotlin)
    implementation(libs.retrofit)
    ksp(libs.moshi)
}
