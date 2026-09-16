package com.unal.senti_ma.domain.repository

import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.Coordinates

interface GeocodingRepository {

    suspend fun getCoordinatesFromAddress(
        address: String
    ): AppResult<Coordinates>

    suspend fun getAddressFromCoordinates(
        coordinates: Coordinates
    ): AppResult<String>

}
