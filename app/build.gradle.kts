plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    // ... other settings ...
    defaultConfig {
        applicationId = "com.HeheJuice.OneUISpoof"
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // LSPosed API - compileOnly ensures it's not packed inside your APK
    compileOnly("de.robv.android.xposed:api:82")
}
