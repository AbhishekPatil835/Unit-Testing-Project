package com.crypto.meracoin.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.crypto.meracoin.utility.SingletonHolder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

private const val CACHE_DIR_SIZE_1MB: Long = 1024 * 1024

class HttpClientFactory private constructor() {

    companion object : SingletonHolder<OkHttpClient, Context>(::createHttpClient) {
        @JvmStatic
        override fun getInstance(arg: Context): OkHttpClient {
            return super.getInstance(arg)
        }
    }
}

private fun createHttpClient(context: Context): OkHttpClient {
    val logLevel = when (BuildConfig.BUILD_TYPE) {
        "release" -> HttpLoggingInterceptor.Level.NONE
        else -> HttpLoggingInterceptor.Level.BODY
    }

    return OkHttpClient.Builder()
        .cache(Cache(context.cacheDir, CACHE_DIR_SIZE_1MB))
        .addNetworkInterceptor(ChuckerInterceptor.Builder(context).build())
        .addNetworkInterceptor(
            HttpLoggingInterceptor().setLevel(logLevel)
        )
        .build()
}
