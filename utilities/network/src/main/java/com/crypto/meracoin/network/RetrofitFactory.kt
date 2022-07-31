package com.crypto.meracoin.network

import android.os.StrictMode
import com.crypto.meracoin.configs.EnvironmentManager
import com.crypto.meracoin.network.NetworkFactory.HostType
import com.crypto.meracoin.network.NetworkFactory.HostType.CONFIG
import com.crypto.meracoin.network.NetworkFactory.HostType.MERACOIN
import com.crypto.meracoin.utility.InitializersExecutor
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory(
    private val environmentManager: EnvironmentManager,
    private val okHttpClientFactory: OkHttpClientFactory
) {
    @Volatile
    private var retrofitCacheMap = mutableMapOf<HostType, Retrofit>()

    init {
        HostType.values().forEach {
            InitializersExecutor.getInstance().execute {
                getRetrofit(it)
            }
        }
    }

    fun getRetrofit(hostType: HostType = MERACOIN): Retrofit {
        if (retrofitCacheMap[hostType] == null) {
            synchronized(NetworkFactory::class.java) {
                if (retrofitCacheMap[hostType] == null) {
                    StrictMode.noteSlowCall("Retrofit initialization slow call")
                    retrofitCacheMap[hostType] = Retrofit.Builder()
                        .baseUrl(getUrl(hostType))
                        .addConverterFactory(GsonConverterFactory.create(Gson()))
                        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                        .client(okHttpClientFactory.getOkHttpClient())
                        .build()
                }
            }
        }
        return retrofitCacheMap[hostType]!!
    }

    private fun getUrl(hostType: HostType): String {
        return when(hostType) {
            MERACOIN -> environmentManager.getServerUrl()
            CONFIG -> environmentManager.getConfigUrl()
        }
    }
}