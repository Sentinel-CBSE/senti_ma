package com.example.senti_ma.domain.repository

import android.app.Activity
import com.example.senti_ma.domain.model.AuthResult
import com.example.senti_ma.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Contract for authentication operations. Implementations decide the actual source
 * (Firebase, a REST API, etc.) — callers only depend on this interface.
 */
interface AuthRepository {

    val authState: Flow<User?>

    fun getCurrentUser(): User?

    suspend fun signOut()

    suspend fun sendPasswordResetEmail(email: String): AuthResult<Unit>

    suspend fun createUserWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
        activity: Activity
    ): AuthResult<User>

    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult<User>

    suspend fun signInWithSavedCredentials(activity: Activity): AuthResult<User>

    suspend fun signInWithGoogle(activity: Activity): AuthResult<User>

    suspend fun signInAnonymously(): AuthResult<User>
}
