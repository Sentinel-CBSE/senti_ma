package com.example.senti_ma.domain.usecase

import android.app.Activity
import com.example.senti_ma.domain.model.AuthResult
import com.example.senti_ma.domain.model.User
import com.example.senti_ma.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Groups all authentication use cases behind a single entry point.
 * ViewModels depend on this instead of the repository directly, keeping
 * the data source (Firebase, or a future REST API) fully isolated.
 */
@Singleton
class AuthUseCases @Inject constructor(
    private val authRepository: AuthRepository
) {

    fun getCurrentUser(): User? =
        authRepository.getCurrentUser()

    suspend fun signOut() =
        authRepository.signOut()

    suspend fun sendPasswordResetEmail(email: String): AuthResult<Unit> =
        authRepository.sendPasswordResetEmail(email)

    suspend fun createUserWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
        activity: Activity
    ): AuthResult<User> =
        authRepository.createUserWithEmailAndPassword(name, email, password, activity)

    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult<User> =
        authRepository.signInWithEmailAndPassword(email, password)

    suspend fun signInWithSavedCredentials(activity: Activity): AuthResult<User> =
        authRepository.signInWithSavedCredentials(activity)

    suspend fun signInWithGoogle(activity: Activity): AuthResult<User> =
        authRepository.signInWithGoogle(activity)

    suspend fun signInAnonymously(): AuthResult<User> =
        authRepository.signInAnonymously()
}
