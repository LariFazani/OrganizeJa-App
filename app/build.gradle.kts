plugins {
    // Aplica o plugin de aplicação Android
    alias(libs.plugins.androidApplication)
    // Aplica o plugin do Kotlin para Android, garantindo uma versão compatível
    alias(libs.plugins.jetbrainsKotlinAndroid)
    // Aplica o plugin do Google Services
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.organizeja"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.organizeja"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:34.2.0"))
    implementation("com.google.firebase:firebase-analytics")
    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth")
    // Firebase Firestore (se você for usar o banco de dados)
    implementation("com.google.firebase:firebase-firestore")
}
