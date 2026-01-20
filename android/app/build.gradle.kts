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
            val path = keystoreProperties.getProperty("storeFile")
            // This looks inside android/app/keystore/
            storeFile = if (path != null) file(path) else null

            storePassword = keystoreProperties.getProperty("storePassword")
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
        }
    }
    flavorDimensions += "version"

    productFlavors {
        create("prod") {
            dimension = "version"
            // You can specify unique IDs or names here if needed
            // applicationIdSuffix = ".prod"
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