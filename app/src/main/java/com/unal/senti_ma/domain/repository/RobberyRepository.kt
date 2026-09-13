package com.unal.senti_ma.domain.repository

import com.unal.senti_ma.domain.model.RobberyPoint
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.MapBounds

interface RobberyRepository {

    suspend fun getRobberyPoints(
        mapBounds: MapBounds,
        fromTimestamp: Long,
        toTimestamp: Long,
        type: String?
    ): AppResult<List<RobberyPoint>>

}
