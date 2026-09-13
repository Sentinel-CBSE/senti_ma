package com.example.senti_ma.data.repository

import android.content.Context
import com.example.senti_ma.R
import com.example.senti_ma.data.remote.api.SentinelApi
import com.example.senti_ma.domain.model.AppResult
import com.example.senti_ma.domain.model.MapBounds
import com.example.senti_ma.domain.model.RobberyPoint
import com.example.senti_ma.domain.repository.RobberyRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
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
        val availableTypes = type?.let { listOf(it) } ?: listOf("armed_robbery", "theft", "burglary")

        return List(count) {
            RobberyPoint(
                latitude = Random.nextDouble(mapBounds.southLat, mapBounds.northLat),
                longitude = Random.nextDouble(mapBounds.westLon, mapBounds.eastLon),
                type = availableTypes.random(),
                timestamp = Random.nextLong(fromTimestamp, toTimestamp)
            )
        }
    }

}
