package com.unal.senti_ma.domain.repository

import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.MapBounds
import com.unal.senti_ma.domain.model.RobberyPoint

interface RobberyRepository {

    suspend fun getRobberyPoints(
        mapBounds: MapBounds,
        fromTimestamp: Long,
        toTimestamp: Long,
        type: String?
    ): AppResult<List<RobberyPoint>>

    suspend fun createRobberyReport(
        type: String,
        latitude: Double,
        longitude: Double,
        timestamp: Long
    ): AppResult<Unit>

}
