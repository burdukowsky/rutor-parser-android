plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "tk.burdukowsky.rutorparserandroid"
    compileSdk = 28

    defaultConfig {
        applicationId = "tk.burdukowsky.rutorparserandroid"
        minSdk = 21
        targetSdk = 28
        versionCode = 3
        versionName = "1.0.2"

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

object Vars {
    const val retrofit2Version = "2.5.0"
}

dependencies {

    implementation("com.squareup.retrofit2:retrofit:${Vars.retrofit2Version}")
    implementation("com.squareup.retrofit2:converter-gson:${Vars.retrofit2Version}")
    implementation("com.android.support:appcompat-v7:28.0.0")
    testImplementation("junit:junit:4.13.2")
}