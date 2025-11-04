plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android) // AGP 8.x에서는 필요
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization) // Kotlinx Serialization
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.autoever.everp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.autoever.everp"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // ViewModel
    implementation(libs.androidx.navigation.compose) // Navigation

    // Jetpack Compose dependencies
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // Debug dependencies
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Retrofit dependencies
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    // Coroutines dependencies
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    // Coil dependency for image loading
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    // Hilt dependencies
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    // Timber dependency for logging
    implementation(libs.timber)
    // Browser (Chrome Custom Tabs)
    implementation(libs.androidx.browser)
//    // DataStore dependencies
//    implementation("androidx.datastore:datastore-preferences:1.0.0")
//    // Room dependencies
//    implementation("androidx.room:room-runtime:2.5.2")
//    kapt("androidx.room:room-compiler:2.5.2")
//    implementation("androidx.room:room-ktx:2.5.2")
//    // Security dependency
//    implementation("androidx.security:security-crypto:1.1.0-alpha03")
//    // Gson dependency for JSON parsing
//    implementation("com.google.code.gson:gson:2.10.1")
//    // Splash Screen dependency
//    implementation("androidx.core:core-splashscreen:1.0.1")
//    // ViewBinding Property Delegate dependency
//    implementation("com.github.Zhuinden:viewbindingpropertydelegate-kt:1.5.6")
}
