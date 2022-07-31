package com.crypto.meracoin.network

import android.content.Context
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient

class OkHttpClientFactory constructor(
    private val context: Context,
    private val dependency: Dependency
) {
    fun getOkHttpClient(): OkHttpClient {
        return HttpClientFactory
            .getInstance(context)
            .newBuilder()
            .apply { addDependencies() }
            .build()
    }

    private fun OkHttpClient.Builder.addDependencies() {
        with(dependency) {
            getInterceptors().forEach {
                addInterceptor(it)
            }
//            if (shouldAttachAuthenticator()) {
//                authenticator(getAuthenticator())
//            }
        }
    }

    private fun shouldAttachAuthenticator(): Boolean = false

    interface Dependency {
        fun getInterceptors(): List<Interceptor>
//        fun getAuthenticator(): Authenticator
    }
}
