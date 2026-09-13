package com.unal.senti_ma.domain.usecase

import android.app.Activity
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.User
import com.unal.senti_ma.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCases @Inject constructor(
    private val authRepository: AuthRepository
) {

    val authState: Flow<User?> = authRepository.authState

    suspend fun signOut() =
        authRepository.signOut()

    suspend fun sendPasswordResetEmail(email: String): AppResult<Unit> =
        authRepository.sendPasswordResetEmail(email)

    suspend fun createUserWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
        activity: Activity
    ): AppResult<User> =
        authRepository.createUserWithEmailAndPassword(name, email, password, activity)

    suspend fun signInWithEmailAndPassword(email: String, password: String): AppResult<User> =
        authRepository.signInWithEmailAndPassword(email, password)

    suspend fun signInWithSavedCredentials(activity: Activity): AppResult<User> =
        authRepository.signInWithSavedCredentials(activity)

    suspend fun signInWithGoogle(activity: Activity): AppResult<User> =
        authRepository.signInWithGoogle(activity)

    suspend fun signInAnonymously(): AppResult<User> =
        authRepository.signInAnonymously()

}
