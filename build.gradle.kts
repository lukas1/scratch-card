// Top-level build file for shared plugin configuration.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kover) apply false
    alias(libs.plugins.detekt) apply false
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}
