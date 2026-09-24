package com.unal.senti_ma.domain.repository

import com.unal.senti_ma.domain.model.AppResult

interface NotificationRepository {

    suspend fun registerInstallationId(
        installationId: String
    ): AppResult<Unit>

}
