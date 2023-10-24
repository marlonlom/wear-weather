import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "dev.marlonlom.codelabs.wear_weather"
  compileSdk = 34

  defaultConfig {
    applicationId = "dev.marlonlom.codelabs.wear_weather"
    minSdk = 30
    //noinspection EditedTargetSdkVersion
    targetSdk = 34
    versionCode = 1
    versionName = "1.0.0"
    vectorDrawables {
      useSupportLibrary = true
    }

    gradleLocalProperties(rootDir).apply {
      buildConfigField("String", "WEATHER_API_BASE_URL", getProperty("WEATHER_API_BASE_URL"))
      buildConfigField("String", "WEATHER_API_KEY", getProperty("WEATHER_API_KEY"))
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true
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
    buildConfig = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.4.3"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {
  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.core:core-splashscreen:1.1.0-alpha02")
  implementation("com.google.android.gms:play-services-wearable:18.1.0")
  implementation("androidx.percentlayout:percentlayout:1.0.0")
  implementation("androidx.legacy:legacy-support-v4:1.0.0")
  implementation("androidx.recyclerview:recyclerview:1.3.2")
  implementation(platform("androidx.compose:compose-bom:2023.03.00"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.wear.compose:compose-material:1.2.1")
  implementation("androidx.wear.compose:compose-foundation:1.2.1")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
  implementation("androidx.activity:activity-compose:1.8.0")
  implementation("androidx.wear:wear-tooling-preview:1.0.0-beta01")
  implementation("androidx.datastore:datastore-preferences:1.0.0")
  implementation("com.google.android.gms:play-services-location:21.0.1")
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")
  implementation("io.coil-kt:coil-compose:2.4.0")
  implementation("androidx.compose.material:material-icons-extended:1.5.4")
  androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")
}
