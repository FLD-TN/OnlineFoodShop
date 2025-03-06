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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
        resources {
            excludes += listOf(
                "META-INF/DEPENDENCIES", // Loại bỏ tệp trùng lặp
                "META-INF/native-image/native-image.properties",
                "META-INF/native-image/reflect-config.json"
            )
        }
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore:25.1.2")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.play.services.location)
    implementation(libs.gridlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.airbnb.android:lottie:6.0.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("org.mongodb:mongodb-driver-sync:5.3.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.google.api-client:google-api-client-android:2.2.0")
    implementation("com.google.apis:google-api-services-drive:v3-rev20230815-2.0.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.http-client:google-http-client-gson:1.43.3")
    implementation ("androidx.gridlayout:gridlayout:1.0.0")
}