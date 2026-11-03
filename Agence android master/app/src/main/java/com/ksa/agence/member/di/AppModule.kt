package com.ksa.agence.member.di

import com.ksa.agence.member.di.AuthenticationInterceptor
import com.ksa.agence.member.di.ConnectivityInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ksa.agence.member.app.AgenceCompanyApp
import com.ksa.agence.common.BASE_URL
import com.ksa.agence.member.common.sharedprefrence.PreferencesUtils
import com.ksa.agence.member.network.APIEndPoint
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


val appModule = module {
    // Qualified names so the member network stack does not override the client one
    single(named("member_base_url")) { BASE_URL }
    single(named("member_retrofit")) { getRetrofit(get(named("member_base_url"))) }
    single { createApi(get(named("member_retrofit"))) }
    single(named("member_okhttp")) { createClient() }
    single { PreferencesUtils(androidContext()) }
}

private val moshi by lazy {
    val moshiBuilder = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
    moshiBuilder.build()
}

private val loggingInterceptor by lazy {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    loggingInterceptor
}

private fun getRetrofit(baseUrl: String): Retrofit {
    return Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(createClient()) // Using Koin to get OkHttpClient
        .build()
}

fun createApi(retrofit: Retrofit): APIEndPoint {
    return retrofit.create(APIEndPoint::class.java)
}

fun createClient(): OkHttpClient {
    return OkHttpClient.Builder().addInterceptor(loggingInterceptor)
        .addInterceptor(AuthenticationInterceptor(AgenceCompanyApp.context!!))
        .addInterceptor(ConnectivityInterceptor(AgenceCompanyApp.context!!))
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
}
