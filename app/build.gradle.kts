plugins {
    id("com.android.application")
}

android {
    namespace = "com.tianyilianmeng.video"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.tianyilianmeng.video"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation ("org.jsoup:jsoup:1.10.2")
    implementation ("xyz.doikki.android.dkplayer:dkplayer-ui:3.3.7")
    implementation ("xyz.doikki.android.dkplayer:dkplayer-java:3.3.7")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}