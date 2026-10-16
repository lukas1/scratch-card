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
    description = "Runs build, static checks, tests, and coverage for both service flavors, plus connected Android tests."
    dependsOn(
        ":app:assembleMockDebug",
        ":app:assembleProductionDebug",
        ":app:detekt",
        ":app:lintMockDebug",
        ":app:lintProductionDebug",
        ":app:testMockDebugUnitTest",
        ":app:testProductionDebugUnitTest",
        ":app:koverHtmlReportMockDebug",
        ":app:koverHtmlReportProductionDebug",
        ":app:koverVerifyPackageCoverage",
        ":app:connectedMockDebugAndroidTest",
    )
}
