plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    alias(libs.plugins.compose.compiler)
//    Push Notifications
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.katielonsdale.chatterbox"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.katielonsdale.chatterbox"
        minSdk = 28
        targetSdk = 34
        versionCode = 26
        versionName = "4.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            // Development URL (emulator localhost)
            buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:3000/api/v0/\"")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
            
            // Production URL
            buildConfigField("String", "API_BASE_URL", "\"https://chatter-box-be-c1487dd4c370.herokuapp.com/api/v0/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }
    
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
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
    implementation(libs.androidx.ui.tooling.android)
    implementation(libs.play.services.tasks)

// Testing
testImplementation(libs.junit)
testImplementation(libs.mockito.core)
testImplementation(libs.mockito.kotlin)
testImplementation(libs.coroutines.test)
testImplementation(libs.arch.core.testing)

androidTestImplementation(libs.androidx.junit)
androidTestImplementation(libs.androidx.espresso.core)
androidTestImplementation(libs.mockito.android)
androidTestImplementation(libs.mockito.kotlin)
androidTestImplementation(libs.arch.core.testing)

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

    // auto link text
    implementation(libs.autolinktext)

//    implementation(libs.coreLibraryDesugaring)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
}