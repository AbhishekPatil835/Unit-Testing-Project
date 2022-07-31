package com.crypto.meracoin.di

import android.content.Context
import com.crypto.meracoin.configs.EnvironmentManager
import com.crypto.meracoin.configs.EnvironmentManagerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ConfigsModule {

    @Provides
    fun providesEnvironmentManager(@ApplicationContext context: Context): EnvironmentManager {
        return EnvironmentManagerFactory.initialize(context)
    }
}