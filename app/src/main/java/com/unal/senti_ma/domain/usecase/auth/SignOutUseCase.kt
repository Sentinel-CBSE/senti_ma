package com.unal.senti_ma.domain.usecase.auth

import com.unal.senti_ma.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke() {
        authRepository.signOut()
    }

}
