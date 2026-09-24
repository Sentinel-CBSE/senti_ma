package com.unal.senti_ma.domain.usecase.notification

import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.repository.NotificationRepository
import javax.inject.Inject

class RegisterDeviceTokenUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {

    suspend operator fun invoke(
        token: String
    ): AppResult<Unit> {
        return notificationRepository.registerDeviceToken(token)
    }

}
