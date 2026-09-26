plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.example.pdfteachingrecorder"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.pdfteachingrecorder"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}
