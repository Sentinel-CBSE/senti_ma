package com.unal.senti_ma.data.repository

import android.content.Context
import android.util.Log
import com.unal.senti_ma.R
import com.unal.senti_ma.data.mappers.toDomain
import com.unal.senti_ma.data.remote.api.SentinelApi
import com.unal.senti_ma.data.remote.dto.RobberyReportRequestDto
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.MapBounds
import com.unal.senti_ma.domain.model.RobberyPoint
import com.unal.senti_ma.domain.repository.RobberyRepository
import com.unal.senti_ma.utils.logIfError
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
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

        Log.d(
            "RobberyRepository",
            "Getting robbery points"
        )

        return try {

            val response = sentinelApi.getRobberyPoints(
                northLat = mapBounds.northLat,
                southLat = mapBounds.southLat,
                eastLon = mapBounds.eastLon,
                westLon = mapBounds.westLon,
                fromTimestamp = fromTimestamp,
                toTimestamp = toTimestamp,
                type = type
            )

            if (response.logIfError(
                    "RobberyRepository",
                    "Get robbery points"
                )
            ) {

                val points = response.body()
                    ?.map { it.toDomain() }
                    ?: emptyList()

                Log.d(
                    "RobberyRepository",
                    "Retrieved ${points.size} robbery points"
                )

                AppResult.Success(points)

            } else {
                AppResult.Error(
                    context.getString(
                        R.string.text_error_get_heatmap
                    )
                )
            }

        } catch (exception: Exception) {

            Log.e(
                "RobberyRepository",
                "Error getting robbery points",
                exception
            )

            AppResult.Error(
                context.getString(R.string.text_error_get_heatmap)
            )
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

        Log.d(
            "RobberyRepository",
            "Creating robbery report: " +
                    "latitude=$latitude, " +
                    "longitude=$longitude, " +
                    "type=$type"
        )

        return try {
            val response = sentinelApi.createRobberyReport(
                RobberyReportRequestDto(
                    type = type,
                    latitude = latitude,
                    longitude = longitude,
                    timestamp = timestamp
                )
            )

            if (response.logIfError(
                    "RobberyRepository",
                    "Create robbery report"
                )
            ) {

                Log.d(
                    "RobberyRepository",
                    "Robbery report created successfully"
                )

                AppResult.Success(Unit)

            } else {
                AppResult.Error(
                    context.getString(
                        R.string.text_error_create_robbery
                    )
                )
            }

        } catch (exception: Exception) {

            Log.e(
                "RobberyRepository",
                "Error creating robbery report",
                exception
            )

            AppResult.Error(
                context.getString(
                    R.string.text_error_create_robbery
                )
            )
        }
    }

}
