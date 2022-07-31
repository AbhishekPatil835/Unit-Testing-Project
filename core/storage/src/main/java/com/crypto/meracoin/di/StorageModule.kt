package com.crypto.meracoin.di

import android.content.Context
import com.crypto.meracoin.storage.PreferenceHandler
import com.crypto.meracoin.storage.PreferenceHandlerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    @Singleton
    fun providesPreferenceHandler(
        @ApplicationContext context: Context
    ): PreferenceHandler {
        return PreferenceHandlerImpl(context)
    }
}