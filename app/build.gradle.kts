plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    kotlin("kapt")
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
}

android {
    namespace = "com.cihatakyol.sleeptimer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cihatakyol.sleeptimer"
        minSdk = 26
        targetSdk = 34

        // Dynamic version code for CI/CD: base version + GitHub run number
        val baseVersionCode = 1
        val ciRunNumber = System.getenv("GITHUB_RUN_NUMBER")?.toIntOrNull() ?: 0
        versionCode = (baseVersionCode * 1000) + ciRunNumber

        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    // Flavor dimensions
    flavorDimensions += "environment"

    // Product flavors for different build variants
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"

            // Test AdMob IDs
            buildConfigField("String", "ADMOB_APP_ID", "\"ca-app-pub-3940256099942544~3347511713\"")
            buildConfigField("String", "ADMOB_BANNER_ID", "\"ca-app-pub-3940256099942544/6300978111\"")
            buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "ADMOB_APP_OPEN_ID", "\"ca-app-pub-3940256099942544/9257395921\"")
            buildConfigField("String", "BUILD_ENVIRONMENT", "\"dev\"")

            // Manifest placeholders for AdMob
            manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713"
        }

        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"

            // Test AdMob IDs (same as test)
            buildConfigField("String", "ADMOB_APP_ID", "\"ca-app-pub-3940256099942544~3347511713\"")
            buildConfigField("String", "ADMOB_BANNER_ID", "\"ca-app-pub-3940256099942544/6300978111\"")
            buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "ADMOB_APP_OPEN_ID", "\"ca-app-pub-3940256099942544/9257395921\"")
            buildConfigField("String", "BUILD_ENVIRONMENT", "\"staging\"")

            // Manifest placeholders for AdMob
            manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713"
        }

        create("production") {
            dimension = "environment"
            // No suffix for production - uses base applicationId

            // Production AdMob IDs from environment variables (set in CI) or default to test IDs
            val admobAppIdProd = System.getenv("ADMOB_APP_ID_PROD") ?: "ca-app-pub-3940256099942544~3347511713"
            val admobBannerIdProd = System.getenv("ADMOB_BANNER_ID_PROD") ?: "ca-app-pub-3940256099942544/6300978111"
            val admobInterstitialIdProd = System.getenv("ADMOB_INTERSTITIAL_ID_PROD") ?: "ca-app-pub-3940256099942544/1033173712"
            val admobAppOpenIdProd = System.getenv("ADMOB_APP_OPEN_ID_PROD") ?: "ca-app-pub-3940256099942544/9257395921"

            buildConfigField("String", "ADMOB_APP_ID", "\"$admobAppIdProd\"")
            buildConfigField("String", "ADMOB_BANNER_ID", "\"$admobBannerIdProd\"")
            buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"$admobInterstitialIdProd\"")
            buildConfigField("String", "ADMOB_APP_OPEN_ID", "\"$admobAppOpenIdProd\"")
            buildConfigField("String", "BUILD_ENVIRONMENT", "\"production\"")

            // Manifest placeholders for AdMob
            manifestPlaceholders["admobAppId"] = admobAppIdProd
        }
    }

    // Signing configs
    signingConfigs {
        create("staging") {
            storeFile = file("../keystores/staging.jks")
            storePassword = System.getenv("STAGING_KEYSTORE_PASSWORD") ?: "staging123"
            keyAlias = System.getenv("STAGING_KEY_ALIAS") ?: "staging"
            keyPassword = System.getenv("STAGING_KEY_PASSWORD") ?: "staging123"
        }

        create("release") {
            storeFile = file("../keystores/release.jks")
            storePassword = System.getenv("RELEASE_KEYSTORE_PASSWORD") ?: ""
            keyAlias = System.getenv("RELEASE_KEY_ALIAS") ?: ""
            keyPassword = System.getenv("RELEASE_KEY_PASSWORD") ?: ""
        }
    }

    buildTypes {
        debug {
            // Test flavor uses debug build type
            isDebuggable = true
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Configure signing for each flavor's release build
    androidComponents {
        onVariants { variant ->
            when (variant.flavorName) {
                "staging" -> {
                    if (variant.buildType == "release") {
                        variant.signingConfig.setConfig(signingConfigs.getByName("staging"))
                    }
                }
                "production" -> {
                    if (variant.buildType == "release") {
                        variant.signingConfig.setConfig(signingConfigs.getByName("release"))
                    }
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Accompanist Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

    // AdMob
    implementation("com.google.android.gms:play-services-ads:23.0.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.9.0")
    implementation(libs.hilt.navigation.compose)

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Firebase - Add Firebase BoM for version management
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")
}

// Apply Google Services plugin at the end - only for production builds
// Dev and Staging can use the same Firebase project as production
// The google-services plugin will only work for the production flavor
// For dev and staging, we'll handle Firebase configuration differently or skip it
if (gradle.startParameter.taskRequests.toString().contains("Production")) {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}
