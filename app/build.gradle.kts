plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.unal.senti_ma"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.unal.senti_ma"
        minSdk = 33
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "SENTI_BACK_BASE_URL",
            "\"${System.getenv("SENTI_BACK_BASE_URL") ?: "https://sentiapigateway.azure-api.net"}\""
        )

        buildConfigField(
            "String",
            "SENTI_APIM_SUBSCRIPTION_KEY",
            "\"${System.getenv("SENTI_APIM_SUBSCRIPTION_KEY") ?: "64e3f822fd0644659c7ff3f464fec097"}\""
        )
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

    lint {
        disable += "CredManMissingDal"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    val composeBom = platform(libs.androidx.compose.bom)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.com.hilt.android)
    ksp(libs.com.hilt.android.compiler)

    implementation(libs.com.googleId)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.com.play.services.auth)
    implementation(libs.com.play.services.base)

    implementation(libs.io.coil.compose)
    implementation(libs.io.coil.network.okhttp)

    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.gson)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.okhttp.logging)

    implementation(libs.org.osmdroid.android)
    implementation(libs.com.osmbonuspack) {
        exclude(group = "com.android.support", module = "support-v4")
    }

    implementation(libs.play.services.location)

    implementation(composeBom)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material.icons.extended)

    implementation(platform(libs.com.firebase.bom))
    implementation(libs.com.firebase.firestore)
    implementation(libs.com.firebase.analytics)
    implementation(libs.com.firebase.messaging)
    implementation(libs.com.firebase.storage)
    implementation(libs.com.firebase.auth)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(composeBom)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
