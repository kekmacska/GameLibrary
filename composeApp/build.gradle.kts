import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "org.kekmacska.gamelibrary"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.kekmacska.gamelibrary"
        minSdk = libs.versions.android.minSdk.get().toInt()
        //noinspection OldTargetApi
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        val localProps = Properties()
        localProps.load(project.rootProject.file("local.properties").inputStream())
        val apiUrl = localProps.getProperty("API_URL") ?: ""

        buildConfigField("String", "API_URL", "\"$apiUrl\"")
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    //AndroidX Core
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    //Compose UI
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewmodelCompose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime)
    implementation(libs.materialIcons)

    //Navigation
    implementation(libs.navi)

    //Storage
    implementation(libs.datastorePreferences)

    //Ktor
    implementation(libs.ktorClientNegotiation)
    implementation(libs.ktorCore)
    implementation(libs.ktorLogging)
    implementation(libs.ktorOkhttp)
    implementation(libs.ktorSerializationJson)

    //Coil
    implementation(libs.coil)
    implementation(libs.coilCompose)
    implementation(libs.coilOkhttp)

    implementation(libs.biometric)

    implementation(libs.shimmer)
}