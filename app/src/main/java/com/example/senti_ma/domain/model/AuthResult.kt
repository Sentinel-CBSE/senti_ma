package com.example.senti_ma.domain.model

/**
 * Generic result wrapper for authentication operations, independent of the underlying provider.
 */
sealed class AuthResult<out T> {
    data class Success<out T>(val data: T) : AuthResult<T>()
    data class Error(val errorMessage: String) : AuthResult<Nothing>()
}
