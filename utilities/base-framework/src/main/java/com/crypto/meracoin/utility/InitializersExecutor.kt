package com.crypto.meracoin.utility

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class InitializersExecutor private constructor() {

    companion object {
        @Volatile
        private var instance: Executor? = null

        @JvmStatic
        fun getInstance(): Executor {
            return instance ?: synchronized(InitializersExecutor::class.java) {
                instance ?: getExecutor().also { instance = it }
            }
        }

        private fun getExecutor(): Executor {
            return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        }
    }
}
