plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-android")
//    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
    id("com.google.gms.google-services") // إضافة Plugin Google Services
    id("com.google.firebase.crashlytics") // إضافة Plugin Google Services

}

android {
    namespace = "com.ksa.agence"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ksa.agence"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // Services location
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-places:17.0.0")
    implementation("com.google.android.libraries.places:places:3.4.0")


    //firebase
    implementation(platform("com.google.firebase:firebase-bom:30.2.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-messaging:23.1.1")
    implementation("com.google.firebase:firebase-inappmessaging-display")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")


    // Retrofit & OkHttp
    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okio:okio:3.7.0")


    // Moshi for json serialization / deserialization
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    implementation("com.squareup.moshi:moshi-adapters:1.12.0")
    implementation("com.squareup.okio:okio:3.7.0")


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    //koin
    implementation("io.insert-koin:koin-android:3.1.5")
    //permission dexter
    implementation("com.karumi:dexter:6.2.3")

    //alum upload gallery
    implementation("com.yanzhenjie:album:2.1.3")
    implementation("com.github.dhaval2404:imagepicker:2.1")

    //tedimagepicker
    implementation("io.github.ParkSangGwon:tedimagepicker:1.4.2")

    //viewPager2 library
    implementation("androidx.viewpager2:viewpager2:1.1.0-rc01")
    //stepper library
    implementation("com.tbuonomo:dotsindicator:4.3")
    //sdp ssp
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.intuit.ssp:ssp-android:1.1.0")
    //lottie
    implementation("com.airbnb.android:lottie:5.2.0")
    //password
    implementation("com.github.scottyab:showhidepasswordedittext:0.8")
    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //otp
    implementation("com.github.aabhasr1:OtpView:v1.1.2")

    //circleimageview
    implementation("de.hdodenhof:circleimageview:3.1.0")
    //cookiebar2
    implementation("org.aviran.cookiebar2:cookiebar2:1.1.5")
    //simpleratingbar
    implementation("com.iarcuschin:simpleratingbar:0.1.5")
    //Paylink-mobile-android-sdk
    implementation("com.github.Paylink-sa:Paylink-mobile-android-sdk:1.1.0")
    implementation("com.google.guava:guava:31.1-jre")
//    implementation ("com.tougee:recorderview:1.0.0")
    implementation("com.github.3llomi:RecordView:3.1.3")
    implementation("com.google.android.exoplayer:exoplayer:2.15.1")
//    implementation ("com.github.3llomi:RecordView:3.1.2")
//    implementation("com.github.yehiareda4:WaveRecordUtile:1.0.14")

    implementation(project(":wave_record_util"))
//    implementation 'com.github.bingoogolapple:BGAPopupWindow:1.3.1'

    implementation("io.github.ParkSangGwon:tedpermission-normal:3.3.0")
    implementation("io.github.ParkSangGwon:tedpermission-rx3:3.3.0")
    implementation("com.github.chnouman:AwesomeDialog:1.0.5")


}