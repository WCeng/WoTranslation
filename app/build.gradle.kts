plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.wotranslate.hilt)
}

android {
    namespace = "com.wceng.wotranslation"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wceng.wotranslation"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "0.0.0"

        testInstrumentationRunner = "com.wceng.core.testing.WoTestRunner"
    }

    // 添加签名配置块
//    signingConfigs {
//        create("release") {  // 使用 create 方法定义配置名称
//            storeFile = file("my-release-key.jks")
//            storePassword = System.getenv("STORE_PASSWORD")
//            keyAlias = "my-alias"
//            keyPassword = System.getenv("KEY_PASSWORD")
//        }
//    }


    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )


//            signingConfig = signingConfigs.getByName("release") // 关联签名配置
        }
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false // Debug 模式通常不开启混淆
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(project(":feature:translate"))
    implementation(project(":feature:input"))
    implementation(project(":feature:bookmark"))
    implementation(project(":feature:languages"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons)

    debugImplementation("com.guolindev.glance:glance:1.1.0")
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.navigation.testing)
    testImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.androidx.ui.test.manifest)
    testImplementation(libs.robolectric)
    testImplementation(libs.hilt.android.testing)


    androidTestImplementation(project(":core:data-test"))
    androidTestImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:datastore-test"))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.navigation.testing)

}