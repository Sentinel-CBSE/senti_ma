package com.unal.senti_ma.domain.usecase.auth

import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.repository.AuthRepository
import javax.inject.Inject

class SendPasswordResetEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        email: String
    ): AppResult<Unit> =
        authRepository.sendPasswordResetEmail(email)

}
