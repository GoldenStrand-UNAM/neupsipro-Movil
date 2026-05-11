plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    alias(libs.plugins.hilt)
    alias(libs.plugins.ktlint)
}

ktlint {
    android.set(true)
    ignoreFailures.set(false)
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
    }
}

android {
    namespace = "com.app.neupsiproMovil"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.app.neupsiproMovil"
        namespace = "com.app.neupsiproMovil"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    // Corrected Compose dependencies
    implementation(libs.androidx.ui) // Changed from libs.androidx.compose.ui
    implementation(libs.androidx.ui.graphics) // Changed from libs.androidx.compose.ui.graphics
    implementation(libs.androidx.ui.tooling.preview) // Changed from libs.androidx.compose.ui.tooling.preview
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.runtime.saveable)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)

    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.junit)
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4) // Changed from libs.androidx.compose.ui.test.junit4
    debugImplementation(libs.androidx.ui.tooling) // Changed from libs.androidx.compose.ui.tooling
    debugImplementation(libs.androidx.ui.test.manifest) // Changed from libs.androidx.compose.ui.test.manifest
    implementation(libs.androidx.compose.material.icons.extended)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Coil
    implementation(libs.coil.compose)

    // Encrypted Shared Preferences
    implementation(libs.androidx.security.crypto)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Encrypt token
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Material Pull to refresh
    implementation(libs.androidx.material)

    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Mockito Kotlin — crea fakes/mocks de clases y interfaces
    testImplementation("org.mockito:mockito-core:5.2.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")

    // Coroutines test — permite correr suspend functions en tests
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")

    // MockWebServer — levanta un servidor HTTP falso local
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")

    // Retrofit — mismo que usas en producción
    testImplementation("com.squareup.retrofit2:retrofit:2.9.0")
    testImplementation("com.squareup.retrofit2:converter-gson:2.9.0")
}
