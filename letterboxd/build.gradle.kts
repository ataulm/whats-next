@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.build.config)
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
    alias(libs.plugins.ksp)
    id("java-library")
    kotlin("kapt")
}

buildConfig {
    packageName("com.ataulm.letterboxd")

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

private val projectJvmTarget = JavaVersion.VERSION_11
kotlin {
    jvmToolchain(projectJvmTarget.majorVersion.toInt())
}

dependencies {
    implementation(libs.moshi.kotlin)
    implementation(libs.retrofit)
    ksp(libs.moshi)
}
