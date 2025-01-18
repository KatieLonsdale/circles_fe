plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    alias(libs.plugins.compose.compiler)
//    Push Notifications
    alias(libs.plugins.google.services) apply false
}

android {
    namespace = "com.katielonsdale.chatterbox"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.katielonsdale.chatterbox"
        minSdk = 26
        targetSdk = 34
        versionCode = 7
        versionName = "1.1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.retrofit.logging.interceptor)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.security.crypto)
    implementation(libs.bouncycastle)
    implementation(libs.kotlin.std.lib)
    implementation(libs.coil.compose)
    implementation(libs.firebase.messaging.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.compose.bom)
    androidTestImplementation(libs.compose.bom)

    // Material Design 3
    implementation(libs.compose.material3)

    // Android Studio Preview support
    implementation(libs.compose.tooling.preview)
    debugImplementation(libs.compose.debug.tooling)

    // UI Tests
    androidTestImplementation(libs.compose.test.junit)
    debugImplementation(libs.compose.test.manifest)

//    // Optional - Integration with activities
    implementation(libs.compose.activity)
//    // Optional - Integration with ViewModels
    implementation(libs.compose.lifecycle.viewmodel)

    implementation(libs.compose.navigation)

    // Firebase Messaging
    implementation(libs.firebase.bom)
    implementation(libs.firebase.messaging)
}