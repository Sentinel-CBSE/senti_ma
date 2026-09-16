package com.unal.senti_ma.domain.usecase

import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.Coordinates
import com.unal.senti_ma.domain.repository.GeocodingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeocodingUseCases @Inject constructor(
    private val geocodingRepository: GeocodingRepository
) {

    suspend fun getCoordinatesFromAddress(
        address: String
    ): AppResult<Coordinates> {
        return geocodingRepository.getCoordinatesFromAddress(address)
    }

    suspend fun getAddressFromCoordinates(
        coordinates: Coordinates
    ): AppResult<String> {
        return geocodingRepository.getAddressFromCoordinates(coordinates)
    }

}
