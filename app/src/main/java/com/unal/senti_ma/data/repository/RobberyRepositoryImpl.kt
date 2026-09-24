package com.unal.senti_ma.data.repository

import android.content.Context
import android.util.Log
import com.unal.senti_ma.R
import com.unal.senti_ma.data.remote.api.SentinelApi
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.MapBounds
import com.unal.senti_ma.domain.model.RobberyPoint
import com.unal.senti_ma.domain.repository.RobberyRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class RobberyRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sentinelApi: SentinelApi
) : RobberyRepository {

    override suspend fun getRobberyPoints(
        mapBounds: MapBounds,
        fromTimestamp: Long,
        toTimestamp: Long,
        type: String?
    ): AppResult<List<RobberyPoint>> {
        return try {
            // TODO: reemplazar por la llamada real cuando el backend esté listo:
            // val response = withContext(Dispatchers.IO) {
            //     sentinelApi.getRobberyPoints(
            //         mapBounds.northLat, mapBounds.southLat,
            //         mapBounds.eastLon, mapBounds.westLon,
            //         fromTimestamp, toTimestamp, type
            //     )
            // }
            //
            // if (!response.logIfError("RobberyRepository", "Get robbery points")) {
            //     return AppResult.Error(context.getString(R.string.text_error_get_heatmap))
            // }
            //
            // val points = response.body()
            //     ?.map { it.toDomain() }
            //     ?: emptyList()

            val points = withContext(Dispatchers.IO) {
                generateFakePoints(mapBounds, fromTimestamp, toTimestamp, type)
            }

            AppResult.Success(points)
        } catch (_: Exception) {
            AppResult.Error(context.getString(R.string.text_error_get_heatmap))
        }
    }

    private suspend fun generateFakePoints(
        mapBounds: MapBounds,
        fromTimestamp: Long,
        toTimestamp: Long,
        type: String?,
        count: Int = 60
    ): List<RobberyPoint> {
        delay(500.milliseconds)
        val availableTypes =
            type?.let { listOf(it) } ?: listOf("armed_robbery", "theft", "burglary")

        return List(count) {
            RobberyPoint(
                id = UUID.randomUUID().toString(),
                latitude = Random.nextDouble(mapBounds.southLat, mapBounds.northLat),
                longitude = Random.nextDouble(mapBounds.westLon, mapBounds.eastLon),
                type = availableTypes.random(),
                timestamp = Random.nextLong(fromTimestamp, toTimestamp)
            )
        }
    }

    override suspend fun createRobberyReport(
        type: String,
        latitude: Double,
        longitude: Double,
        timestamp: Long
    ): AppResult<Unit> {
        return try {
            // TODO: reemplazar por la llamada real cuando el backend esté listo:
            // val response = withContext(Dispatchers.IO) {
            //     sentinelApi.createRobberyReport(
            //         type = type, latitude = latitude,
            //         longitude = longitude, timestamp = timestamp
            //     )
            // }
            //
            // if (!response.logIfError("RobberyRepository", "Create robbery report")) {
            //     return AppResult.Error(context.getString(R.string.text_error_create_robbery))
            // }

            withContext(Dispatchers.IO) {
                delay(500.milliseconds)

                Log.d(
                    "RobberyRepository",
                    "Sending report: " +
                            "latitude=${latitude}, " +
                            "longitude=${longitude}"
                )
            }

            AppResult.Success(Unit)
        } catch (_: Exception) {
            AppResult.Error(
                context.getString(
                    R.string.text_error_create_robbery
                )
            )
        }
    }

}
