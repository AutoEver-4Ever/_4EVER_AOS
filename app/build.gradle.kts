plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android) // AGP 8.x에서는 필요
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization) // Kotlinx Serialization
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.secrets.gradle.plugin)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
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

    buildFeatures {
        buildConfig = true
        compose = true
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

    secrets {
        // 기본 'local.properties' 대신 다른 파일 이름 지정
        propertiesFileName = "secrets.properties"

        // CI/CD 환경을 위한 기본값 파일 지정 (버전 관리에 포함 가능)
        defaultPropertiesFileName = "secrets.defaults.properties"

        // 특정 키를 무시하도록 정규식 추가 (기본적으로 "sdk.dir"은 무시됨)
        ignoreList.add("keyToIgnore")
        ignoreList.add("ignore*")
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
    implementation(libs.androidx.datastore.preferences) // DataStore Preferences
    implementation(libs.androidx.room.runtime) // Room Runtime
    ksp(libs.androidx.room.compiler) // Room Compiler
    implementation(libs.androidx.room.ktx) // Room KTX

    // Jetpack Compose dependencies
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended) // TODO 배포시 해당 의존성 삭제하고 필요한 아이콘만 개별 추가
    // Debug dependencies
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    // Firebase dependencies
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.perf)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.config)

    // Retrofit dependencies
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)
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
