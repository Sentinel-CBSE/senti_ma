package com.unal.senti_ma.utils

import retrofit2.Response

fun <T> Response<T>.logIfError(tag: String, action: String): Boolean {
    if (!isSuccessful) {
        android.util.Log.e(
            tag,
            "$action failed: HTTP ${code()} - ${errorBody()?.string()}"
        )
    }
    return isSuccessful
}
