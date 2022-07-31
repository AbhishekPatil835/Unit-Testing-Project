package com.crypto.meracoin.utility

import com.google.gson.Gson
import retrofit2.Response

inline fun <reified T> Response<T>.getErrorBody() : T {
    return Gson().fromJson(errorBody()?.string(), T::class.java)
}