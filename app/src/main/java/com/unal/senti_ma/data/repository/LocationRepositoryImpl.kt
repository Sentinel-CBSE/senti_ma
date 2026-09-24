package com.unal.senti_ma.data.repository

import android.content.Context
import android.util.Log
import com.unal.senti_ma.R
import com.unal.senti_ma.data.remote.api.SentinelApi
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.Coordinates
import com.unal.senti_ma.domain.repository.LocationRepository
import com.unal.senti_ma.utils.logIfError
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sentinelApi: SentinelApi
) : LocationRepository {

    override suspend fun sendLocation(
        coordinates: Coordinates
    ): AppResult<Unit> {

        Log.d(
            "LocationRepository",
            "Sending location: " +
                    "latitude=${coordinates.latitude}, " +
                    "longitude=${coordinates.longitude}"
        )

        return try {
            val response = sentinelApi.sendLocation(
                latitude = coordinates.latitude,
                longitude = coordinates.longitude
            )

            if (response.logIfError("LocationRepository", "Send location")) {
                Log.d(
                    "LocationRepository",
                    "Location sent successfully"
                )
                AppResult.Success(Unit)
            } else {
                AppResult.Error(context.getString(R.string.text_error_send_location))
            }

        } catch (exception: Exception) {

            Log.e(
                "LocationRepository",
                "Error sending location",
                exception
            )

            AppResult.Error(
                context.getString(
                    R.string.text_error_send_location
                )
            )
        }
    }

}
