plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kover)
    alias(libs.plugins.detekt)
}

detekt {
    allRules = true
    config.setFrom(files("detekt.yml"))
    buildUponDefaultConfig = true
}

kover {
    currentProject {
        createVariant("packageCoverage") {
            add("mockDebug")
            add("productionDebug")
        }
    }
    reports {
        filters {
            excludes {
                annotatedBy("sk.lukasvyletel.scratchcard.core.annotations.ExcludeFromCoverage")
                classes("*Preview*")
            }
        }
        variant("mockDebug") {
            html {}
        }
        variant("productionDebug") {
            html {}
        }
        variant("packageCoverage") {
            filters {
                includes {
                    packages("sk.lukasvyletel.scratchcard.features")
                    packages("sk.lukasvyletel.scratchcard.core")
                    packages("sk.lukasvyletel.scratchcard.shared")
                }
                excludes {
                    annotatedBy("sk.lukasvyletel.scratchcard.core.annotations.ExcludeFromCoverage")
                    classes("*Preview*")
                }
            }
            verify {
                rule("Features, core, and shared line coverage") { minBound(100) }
            }
        }
    }
}

android {
    namespace = "sk.lukasvyletel.scratchcard"
    compileSdk = 37

    lint {
        warningsAsErrors = true
    }

    defaultConfig {
        applicationId = "sk.lukasvyletel.scratchcard"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "sk.lukasvyletel.scratchcard.ScratchCardAppTestRunner"
    }

    flavorDimensions += "service"
    productFlavors {
        create("mock") {
            dimension = "service"
        }
        create("production") {
            dimension = "service"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    // Android and Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.kotlinx.coroutines.android)

    // Generic HTTP and JSON infrastructure
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.okhttp.logging.interceptor)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.converter.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.json)

    // JVM unit and Compose tests
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.squareup.okhttp.mockwebserver)

    // Android integration tests
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.squareup.okhttp.mockwebserver)

    add("mockImplementation", libs.squareup.okhttp.mockwebserver)

    // Supplies the activity used by Compose tests, including Robolectric.
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
