package com.unal.senti_ma.domain.model

sealed class AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>()
    data class Error(val errorMessage: String) : AppResult<Nothing>()
}
