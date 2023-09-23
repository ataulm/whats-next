plugins {
    @Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
    alias(libs.plugins.android.library) apply false
    @Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
    alias(libs.plugins.android) apply (false)
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.build.config) apply false
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.kotlin.android) apply (false)
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.kotlin.parcelize) apply (false)
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.ksp) apply (false)
}
