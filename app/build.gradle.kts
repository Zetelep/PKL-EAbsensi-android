plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
}

android {
    namespace = "com.zulfa.eabsensi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.zulfa.eabsensi"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Mockito Core
    testImplementation("org.mockito:mockito-core:5.12.0")
// Mockito Kotlin (lebih enak untuk Kotlin)
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
// Untuk coroutines (kalau kamu testing ViewModel pakai suspend function)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")

    implementation(libs.glide.compose)
    implementation(libs.material3)
    implementation(libs.androidx.material3.window.size.class1)
    implementation(libs.androidx.material3.adaptive.navigation.suite)


    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose) // Added this line


    implementation(libs.androidx.datastore.preferences)

    implementation("com.google.android.gms:play-services-maps:19.2.0")
    implementation("com.google.maps.android:maps-compose:6.1.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation(libs.accompanist.permissions)
}