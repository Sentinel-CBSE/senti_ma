package com.unal.senti_ma.domain.repository

import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.Coordinates

interface LocationRepository {

    suspend fun sendLocation(
        coordinates: Coordinates
    ): AppResult<Unit>

}
