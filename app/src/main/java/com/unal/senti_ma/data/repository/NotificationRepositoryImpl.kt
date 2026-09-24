package com.unal.senti_ma.data.repository

import android.content.Context
import android.util.Log
import com.unal.senti_ma.R
import com.unal.senti_ma.data.remote.api.SentinelApi
import com.unal.senti_ma.data.remote.dto.InstallationIdRequestDto
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.repository.NotificationRepository
import com.unal.senti_ma.utils.logIfError
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sentinelApi: SentinelApi
) : NotificationRepository {

    override suspend fun registerInstallationId(
        installationId: String
    ): AppResult<Unit> {

        Log.d(
            "NotificationRepository",
            "Registering installationId"
        )

        return try {
            val response = sentinelApi.registerDeviceToken(
                request = InstallationIdRequestDto(installationId = installationId)
            )

            if (response.logIfError("NotificationRepository", "Register installationId")) {
                Log.d(
                    "NotificationRepository",
                    "InstallationId registered successfully"
                )
                AppResult.Success(Unit)
            } else {
                AppResult.Error(
                    context.getString(R.string.text_error_register_installation_id)
                )
            }

        } catch (exception: Exception) {

            Log.e(
                "NotificationRepository",
                "Error registering installation id",
                exception
            )

            AppResult.Error(
                context.getString(
                    R.string.text_error_register_installation_id
                )
            )
        }
    }

}
