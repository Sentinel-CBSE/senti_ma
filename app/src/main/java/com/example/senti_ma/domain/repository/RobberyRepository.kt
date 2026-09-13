package com.example.senti_ma.domain.repository

import com.example.senti_ma.domain.model.RobberyPoint
import com.example.senti_ma.domain.model.AppResult
import com.example.senti_ma.domain.model.MapBounds

interface RobberyRepository {

    suspend fun getRobberyPoints(
        mapBounds: MapBounds,
        fromTimestamp: Long,
        toTimestamp: Long,
        type: String?
    ): AppResult<List<RobberyPoint>>

}
