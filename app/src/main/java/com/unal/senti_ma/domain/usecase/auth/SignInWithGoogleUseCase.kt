package com.unal.senti_ma.domain.usecase.auth

import android.app.Activity
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.User
import com.unal.senti_ma.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        activity: Activity
    ): AppResult<User> =
        authRepository.signInWithGoogle(activity)

}
