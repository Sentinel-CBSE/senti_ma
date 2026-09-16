package com.unal.senti_ma.data.repository

import android.content.Context
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
            val points = withContext(Dispatchers.IO) {
                // TODO: reemplazar por la llamada real cuando el backend esté listo:
                // sentinelApi.getRobberyPoints(bounds.northLat, bounds.southLat, bounds.eastLon, bounds.westLon, type, fromTimestamp, toTimestamp).map { it.toDomain() }
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
            withContext(Dispatchers.IO) {
                // TODO: reemplazar por la llamada real:
                // sentinelApi.createRobberyReport(type = type, latitude = latitude, longitude = longitude, timestamp = timestamp)
                delay(500.milliseconds)
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
