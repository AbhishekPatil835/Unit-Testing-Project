package com.crypto.meracoin.di

import android.content.Context
import com.crypto.meracoin.configs.EnvironmentManager
import com.crypto.meracoin.network.NetworkFactory
import com.crypto.meracoin.network.NetworkFactoryImpl
import com.crypto.meracoin.network.OkHttpClientFactory
import com.crypto.meracoin.network.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkFactory(retrofitFactory: RetrofitFactory): NetworkFactory {
        return NetworkFactoryImpl(retrofitFactory)
    }

    @Provides
    @Singleton
    fun providesRetrofitFactory(
        environmentManager: EnvironmentManager,
        okHttpClientFactory: OkHttpClientFactory
    ): RetrofitFactory {
        return RetrofitFactory(environmentManager, okHttpClientFactory)
    }

    @Provides
    @Singleton
    fun providesOkHttpClientFactory(
        @ApplicationContext context: Context,
        dependency: OkHttpClientFactory.Dependency
    ) : OkHttpClientFactory{
        return OkHttpClientFactory(context, dependency)
    }
}