import org.gradle.kotlin.dsl.androidTestImplementation
import org.gradle.kotlin.dsl.debugImplementation

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
        minSdk = 28
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

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "META-INF/NOTICE.md",
                "META-INF/NOTICE",
                "META-INF/LICENSE",
            )
        }
    }
}

dependencies {

    // ── Core ──────────────────────────────────────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // ── Compose BOM (maneja todas las versiones de Compose automáticamente) ──
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.runtime.saveable)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material.icons.extended)

    // ── Hilt ──────────────────────────────────────────────────────────────────
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // ── Retrofit ──────────────────────────────────────────────────────────────
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // ── Coil ──────────────────────────────────────────────────────────────────
    implementation(libs.coil.compose)
    implementation("io.coil-kt:coil-compose:2.5.0")

    // ── Seguridad y almacenamiento ────────────────────────────────────────────
    implementation(libs.androidx.security.crypto)
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation(libs.androidx.datastore.preferences)

    // ── Coroutines ────────────────────────────────────────────────────────────
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // ── Material ──────────────────────────────────────────────────────────────
    implementation(libs.androidx.material)

    // ── Showcase Layout ───────────────────────────────────────────────────────
    implementation("ly.com.tahaben:showcase-layout-compose:1.0.9")

    // ── Unit Tests (src/test/) — corren en JVM, sin emulador ─────────────────
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.17")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")

    // ── Instrumented Tests (src/androidTest/) — corren en emulador ───────────
    androidTestImplementation(platform(libs.androidx.compose.bom))  // BOM también para tests
    androidTestImplementation(libs.androidx.junit)                   // versión resuelta por BOM
    androidTestImplementation(libs.androidx.espresso.core)           // versión resuelta por BOM
    androidTestImplementation(libs.androidx.ui.test.junit4)          // Compose UI testing
    androidTestImplementation("org.mockito:mockito-android:5.4.0")
    androidTestImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")

    // ── Debug ─────────────────────────────────────────────────────────────────
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")

    // Retrofit + Gson (si no los tienes ya)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Coroutines test (para runTest)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")

    // JUnit 4
    testImplementation("junit:junit:4.13.2")


    testImplementation("org.mockito:mockito-core:5.4.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testImplementation(kotlin("test"))
}