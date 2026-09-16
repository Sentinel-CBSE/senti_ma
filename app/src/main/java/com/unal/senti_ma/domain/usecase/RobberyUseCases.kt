package com.unal.senti_ma.domain.usecase

import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.HeatmapPoint
import com.unal.senti_ma.domain.model.MapBounds
import com.unal.senti_ma.domain.model.RobberyMapData
import com.unal.senti_ma.domain.model.RobberyPoint
import com.unal.senti_ma.domain.repository.RobberyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RobberyUseCases @Inject constructor(
    private val robberyRepository: RobberyRepository
) {

    companion object {
        private const val GRID_SIZE = 40
        private const val MIN_INTENSITY = 0.25f
        private const val FAR_PAST_TIMESTAMP = 0L // 1 ene 1970:
    }

    suspend fun getRobberyMapData(
        mapBounds: MapBounds,
        fromTimestamp: Long?,
        toTimestamp: Long?,
        type: String?
    ): AppResult<RobberyMapData> {

        val effectiveFrom =
            fromTimestamp ?: FAR_PAST_TIMESTAMP

        val effectiveTo =
            toTimestamp ?: System.currentTimeMillis()

        return when (
            val result = robberyRepository.getRobberyPoints(
                mapBounds = mapBounds,
                fromTimestamp = effectiveFrom,
                toTimestamp = effectiveTo,
                type = type
            )
        ) {
            is AppResult.Success -> {
                val robberies = result.data

                AppResult.Success(
                    RobberyMapData(
                        robberyPoints = robberies,
                        heatmapPoints = aggregateIntoGrid(
                            points = robberies,
                            mapBounds = mapBounds
                        )
                    )
                )
            }

            is AppResult.Error -> result

            AppResult.Cancelled -> AppResult.Cancelled
        }
    }

    private fun aggregateIntoGrid(
        points: List<RobberyPoint>,
        mapBounds: MapBounds
    ): List<HeatmapPoint> {
        if (points.isEmpty()) return emptyList()

        val latStep = (mapBounds.northLat - mapBounds.southLat) / GRID_SIZE
        val lonStep = (mapBounds.eastLon - mapBounds.westLon) / GRID_SIZE
        if (latStep <= 0 || lonStep <= 0) return emptyList()

        val cells = points.groupBy { point ->
            val row =
                ((point.latitude - mapBounds.southLat) / latStep).toInt().coerceIn(0, GRID_SIZE - 1)
            val col =
                ((point.longitude - mapBounds.westLon) / lonStep).toInt().coerceIn(0, GRID_SIZE - 1)
            row to col
        }

        val maxCount = cells.values.maxOf { it.size }

        return cells.map { (_, cellPoints) ->
            HeatmapPoint(
                latitude = cellPoints.map { it.latitude }.average(),
                longitude = cellPoints.map { it.longitude }.average(),
                intensity = (cellPoints.size.toFloat() / maxCount).coerceIn(MIN_INTENSITY, 1f)
            )
        }
    }

    suspend fun createRobberyReport(
        type: String,
        latitude: Double,
        longitude: Double
    ): AppResult<Unit> {
        return robberyRepository.createRobberyReport(
            type = type,
            latitude = latitude,
            longitude = longitude,
            timestamp = System.currentTimeMillis()
        )
    }

}
