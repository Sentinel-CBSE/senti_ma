package com.unal.senti_ma.domain.usecase.auth

import com.unal.senti_ma.domain.model.User
import com.unal.senti_ma.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator fun invoke(): Flow<User?> =
        authRepository.authState

}
