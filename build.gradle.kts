// Top-level build file for shared plugin configuration.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kover) apply false
    alias(libs.plugins.detekt) apply false
}

tasks.register<Delete>("clean") {
    description = "Cleans build directory"
    delete(layout.buildDirectory)
}

tasks.register("qualityGate") {
    group = "verification"
    description = "Runs the app build, static checks, tests, coverage report, and connected Android tests."
    dependsOn(
        ":app:assembleDebug",
        ":app:detekt",
        ":app:lintDebug",
        ":app:test",
        ":app:koverHtmlReportDebug",
        ":app:connectedDebugAndroidTest",
    )
}
