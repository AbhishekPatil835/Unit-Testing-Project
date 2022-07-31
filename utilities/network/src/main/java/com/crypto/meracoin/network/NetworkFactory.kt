package com.crypto.meracoin.network

import com.crypto.meracoin.network.NetworkFactory.HostType
import com.crypto.meracoin.network.NetworkFactory.HostType.MERACOIN

interface NetworkFactory {
    fun <T> getService(hostType: HostType, serviceClass: Class<T>): T
    fun <T> getService(serviceClass: Class<T>): T = getService(MERACOIN, serviceClass)

    enum class HostType {
        MERACOIN, CONFIG
    }
}

class NetworkFactoryImpl(private val retrofitFactory: RetrofitFactory) : NetworkFactory {
    override fun <T> getService(hostType: HostType, serviceClass: Class<T>): T {
        return retrofitFactory.getRetrofit(hostType).create(serviceClass)
    }
}
