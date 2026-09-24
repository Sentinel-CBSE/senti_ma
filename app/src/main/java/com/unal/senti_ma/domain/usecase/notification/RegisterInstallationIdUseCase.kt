package com.unal.senti_ma.domain.usecase.notification

import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.repository.NotificationRepository
import javax.inject.Inject

class RegisterInstallationIdUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {

    suspend operator fun invoke(
        installationId: String
    ): AppResult<Unit> {
        return notificationRepository.registerInstallationId(installationId)
    }

}
