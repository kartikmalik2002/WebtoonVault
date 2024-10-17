plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    id("com.google.gms.google-services")
}


android {
    namespace = "com.example.webtoonvault"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.webtoonvault"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.compose.material:material-icons-core:1.7.3")
    implementation("androidx.compose.material:material-icons-extended:1.7.3")

    // 2. Room Runtime
    implementation(libs.androidx.room.runtime)
    // 3. Room KTX
    implementation(libs.androidx.room.ktx)
    // 4. Room Compiler
    kapt(libs.androidx.room.compiler)

    //Coil
    implementation(libs.coil.compose)

    //Firebase
    implementation(platform(libs.firebase.bom))


    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth-ktx")

    // Google Sign-In
    implementation(libs.play.services.auth)

    // Firebase Firestore
    implementation(libs.firebase.firestore.ktx)

    // Firebase Storage
    implementation(libs.firebase.storage.ktx)

    //Compose Navigation

    implementation (libs.androidx.navigation.compose)
}