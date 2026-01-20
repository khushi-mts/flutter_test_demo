import java.util.Properties
import java.io.FileInputStream

// 1. FIX PATH: rootProject usually refers to the 'android' folder.
// If your file is 'android/key_v1.jks.properties', use that name exactly.
val keystorePropertiesFile = rootProject.file("../key.properties")
val keystoreProperties = Properties()

if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "com.example.flutter_test_demo"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        // 2. FIX DEPRECATION: Use a simple string "17"
        jvmTarget = "17"
    }

    defaultConfig {
        applicationId = "com.example.flutter_test_demo"
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    signingConfigs {
        create("release") {
            // 3. FIX PATH: If the file is in android/app/keystore/keys.keystore:
            storeFile = rootProject.file("app/keystore/keys.keystore")

            // 4. FIX CASTING & TYPE: Use getProperty to avoid "No cast needed" errors
            storePassword = keystoreProperties.getProperty("storePassword")
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
}

flutter {
    source = "../.."
}