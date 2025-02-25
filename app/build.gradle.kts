plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    signingConfigs {
        create("OnlineFoodShop_Config") {
            storeFile = file("C:\\Users\\Admin\\Desktop\\OnlineFoodShop_Keystore.jks")
            storePassword = "999999"
            keyAlias = "duyne"
            keyPassword = "999999"
        }
    }
    namespace = "com.sinhvien.onlinefoodshop"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sinhvien.onlinefoodshop"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("OnlineFoodShop_Config")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-auth")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.airbnb.android:lottie:6.0.0")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
}