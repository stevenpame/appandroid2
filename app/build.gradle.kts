import org.gradle.kotlin.dsl.invoke

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.pipe.avi"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.pipe.avi"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    packagingOptions {
        jniLibs {
            useLegacyPackaging = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    // AndroidX básicas
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.webkit:webkit:1.12.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // UI
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.browser:browser:1.5.0")

    // Imágenes
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.davemorrissey.labs:subsampling-scale-image-view:3.10.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Charts
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Cloudinary
    implementation("com.cloudinary:cloudinary-android:2.3.1")

    //avatar
    implementation("com.facebook.fresco:fresco:3.6.0")

}
