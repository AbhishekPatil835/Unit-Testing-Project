package com.crypto.meracoin.configs

import android.content.Context
import com.crypto.meracoin.configs.CommonKeys.KEY_BASE_URL
import com.crypto.meracoin.configs.EnvironmentManagerFactory.Companion.initialize
import com.crypto.meracoin.storage.PreferenceHandler
import com.crypto.meracoin.storage.PreferenceHandlerImpl
import com.crypto.meracoin.storage.SharedPrefKey
import com.crypto.meracoin.utility.SingletonHolder

interface EnvironmentManager {
    fun getServerUrl(): String
    fun getConfigUrl(): String
}

class EnvironmentManagerFactory private constructor() {

    companion object : SingletonHolder<EnvironmentManager, Context>(::initialize) {

        fun initialize(context: Context): EnvironmentManager {
            return if (BuildConfig.DEBUG) {
                EnvironmentManagerDebug(context)
            } else {
                EnvironmentManagerImpl(context)
            }
        }

        @JvmStatic
        override fun getInstance(arg: Context): EnvironmentManager {
            return super.getInstance(arg)
        }
    }

    private class EnvironmentManagerImpl(context: Context) : EnvironmentManager {

        val preferenceHandler : PreferenceHandler = PreferenceHandlerImpl(
            context,
            SharedPrefKey.ENVIRONMENTS_PREF
        )

        override fun getServerUrl() = preferenceHandler.readString(KEY_BASE_URL, BuildConfig.KEY_BASE_URL)

        override fun getConfigUrl() = BuildConfig.KEY_ANDROID_CONFIG_URL
    }

    private class EnvironmentManagerDebug(context: Context) : EnvironmentManager {
        val preferenceHandler : PreferenceHandler = PreferenceHandlerImpl(
            context,
            SharedPrefKey.ENVIRONMENTS_PREF
        )

        override fun getServerUrl() = preferenceHandler.readString(KEY_BASE_URL, BuildConfig.KEY_BASE_URL)

        override fun getConfigUrl() = BuildConfig.KEY_ANDROID_CONFIG_URL
    }
}