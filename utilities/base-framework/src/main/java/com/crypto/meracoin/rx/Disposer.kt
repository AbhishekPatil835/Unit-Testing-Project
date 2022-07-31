package com.crypto.meracoin.rx

interface Disposer<T> {
    fun T.collect()
    fun dispose()
}