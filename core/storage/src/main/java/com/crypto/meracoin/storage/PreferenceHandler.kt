package com.crypto.meracoin.storage

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.rxjava3.exceptions.CompositeException
import timber.log.Timber
import java.lang.reflect.Type

/**
 * Implements preference layer.
 */
interface PreferenceHandler {
    fun readString(key: String, default: String = ""): String
    fun writeString(key: String, value: String)
    fun readLong(key: String, default: Long = 0L): Long
    fun writeLong(key: String, value: Long)
    fun readInteger(key: String, default: Int = 0): Int
    fun writeInteger(key: String, value: Int)
    fun readBoolean(key: String, default: Boolean = false): Boolean
    fun writeBoolean(key: String, value: Boolean)
    fun readFloat(key: String, default: Float = 0f): Float
    fun writeFloat(key: String, value: Float)
    fun readDouble(key: String, default: Double = 0.0): Double
    fun writeDouble(key: String, value: Double)
    fun readStringSet(key: String, default: Set<String> = mutableSetOf()): Set<String>
    fun writeStringSet(key: String, value: Set<String>)
    fun <T> readInstance(key: String, className: Class<T>): T?
    fun <T> readInstance(key: String, type: Type): T?
    fun writeInstance(key: String, classObjects: Any)

    // Synchronous methods
    fun writeBooleanSync(key: String, value: Boolean): Boolean
    fun writeInstanceSync(key: String, classObjects: Any): Boolean

    fun remove(key: String)
    fun has(key: String): Boolean
    fun getAll(): Map<String, *>
    fun clear()
}

class PreferenceHandlerImpl (
    context: Context,
    private val prefKey: SharedPrefKey = SharedPrefKey.USER_SHARED_PREF
) : PreferenceHandler {

    private val gson = Gson()

    private val sharedPreferences by lazy {
        val ctx = context.applicationContext
        ctx.getSharedPreferences(prefKey.fileName, Context.MODE_PRIVATE)
    }

    override fun <T> readInstance(key: String, className: Class<T>): T? =
        try {
            gson.fromJson(readString(key), className)
        } catch (e: JsonSyntaxException) {
            val exception = CompositeException(
                e,
                RuntimeException("readInstance error: Key: $key :: Class:: $className")
            )
            Timber.e(exception.stackTrace.toString())
            null
        }

    override fun <T> readInstance(key: String, type: Type): T? =
        try {
            gson.fromJson(readString(key), type)
        } catch (e: JsonSyntaxException) {
            val exception =
                CompositeException(
                    e,
                    RuntimeException("readInstance error: Key: $key :: Type:: $type")
                )

            Timber.e(exception.stackTrace.toString())
            null
        }

    override fun writeInstance(key: String, classObjects: Any) {
        writeString(key, gson.toJson(classObjects))
    }

    override fun readString(key: String, default: String): String =
        if (has(key)) getPreference(key, default) else default

    override fun readLong(key: String, default: Long): Long =
        if (has(key)) getPreference(key, default) else default

    override fun readInteger(key: String, default: Int): Int =
        if (has(key)) getPreference(key, default) else default

    override fun readFloat(key: String, default: Float): Float =
        if (has(key)) getPreference(key, default) else default

    override fun readDouble(key: String, default: Double): Double =
        if (has(key)) getPreference(key, default.toLong()).toDouble() else default

    override fun readBoolean(key: String, default: Boolean) = getPreference(key, default)

    override fun readStringSet(key: String, default: Set<String>): Set<String> =
        if (has(key)) getPreference(key, setOf()) else setOf()

    override fun writeString(key: String, value: String) = setPreference(key, value)
    override fun writeLong(key: String, value: Long) = setPreference(key, value)
    override fun writeInteger(key: String, value: Int) = setPreference(key, value)
    override fun writeBoolean(key: String, value: Boolean) = setPreference(key, value)
    override fun writeFloat(key: String, value: Float) = setPreference(key, value)
    override fun writeDouble(key: String, value: Double) = setPreference(key, value.toLong())
    override fun writeStringSet(key: String, value: Set<String>) = setPreference(key, value)

    override fun remove(key: String) = sharedPreferences.edit().remove(key).apply()

    override fun has(key: String): Boolean = sharedPreferences.contains(key)

    override fun clear() = sharedPreferences.edit().clear().apply()

    /**
     * Synchronous methods.
     */
    override fun writeBooleanSync(key: String, value: Boolean) = setPreferenceSync(key, value)

    override fun writeInstanceSync(key: String, classObjects: Any) = writeStringSync(key, gson.toJson(classObjects))

    private fun writeStringSync(key: String, value: String) = setPreferenceSync(key, value)

    @Suppress("UNCHECKED_CAST")
    private fun <T> getPreference(key: String, default: T): T = with(sharedPreferences) {
        val res: Any = when (default) {
            is Long -> getLong(key, default)
            is String -> getString(key, default as String) as String
            is Int -> getInt(key, default as Int)
            is Boolean -> getBoolean(key, default as Boolean)
            is Float -> getFloat(key, default as Float)
            is Set<*> -> getStringSet(key, default as Set<String>) as Set<String>
            else -> throw IllegalArgumentException("No argument matches")
        }
        res as T
    }

    private fun <T> setPreference(key: String, value: T) = with(sharedPreferences.edit()) {
        when (value) {
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Set<*> -> putStringSet(key, value as Set<String>)
            else -> throw IllegalArgumentException("No Argument matches")
        }.apply()
    }

    private fun <T> setPreferenceSync(key: String, value: T) = with(sharedPreferences.edit()) {
        when (value) {
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Set<*> -> putStringSet(key, value as Set<String>)
            else -> throw IllegalArgumentException("No Argument matches")
        }.commit()
    }

    override fun getAll(): Map<String, *> = sharedPreferences.all
}
