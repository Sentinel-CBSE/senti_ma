package com.unal.senti_ma.domain.repository

import android.app.Activity
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val authState: Flow<User?>

    fun getCurrentUser(): User?

    suspend fun signOut()

    suspend fun sendPasswordResetEmail(email: String): AppResult<Unit>

    suspend fun createUserWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
        activity: Activity
    ): AppResult<User>

    suspend fun signInWithEmailAndPassword(email: String, password: String): AppResult<User>

    suspend fun signInWithSavedCredentials(activity: Activity): AppResult<User>

    suspend fun signInWithGoogle(activity: Activity): AppResult<User>

    suspend fun signInAnonymously(): AppResult<User>

}
