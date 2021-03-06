package com.impactyoung.applyexchangerate.network

import com.impactyoung.applyexchangerate.BuildConfig
import io.reactivex.schedulers.Schedulers
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceFactory {

    companion object {
        val HOST_URL = BuildConfig.URL_HOST
    }

    fun getOrganApi(url: String): OrganRestApi = getDefaultOkHttpBuilder(url)
        .run {
            val client = build()
            getDefaultRetrofitBuilder(url)
                .client(client)
                .build()
                .create(OrganRestApi::class.java)
        }

    private fun getDefaultOkHttpBuilder(url: String, isIncludeAuth : Boolean = true) = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .apply {
            if (url.startsWith("https")) {
                // Make http client supports All TLS and Cipher suites
                val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .allEnabledTlsVersions()
                    .allEnabledCipherSuites()
                    .supportsTlsExtensions(true)
                    .build()
                connectionSpecs(listOf(spec))
            }
            if(BuildConfig.DEBUG){
                addInterceptor(getLoggingInterceptor())
            }
        }

    private fun getDefaultRetrofitBuilder(url: String) = Retrofit.Builder()
        .baseUrl(url)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create())

    private fun getLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}
