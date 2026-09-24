package com.unal.senti_ma.domain.usecase.geocoding

import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.Coordinates
import com.unal.senti_ma.domain.repository.GeocodingRepository
import javax.inject.Inject

class GetAddressFromCoordinatesUseCase @Inject constructor(
    private val geocodingRepository: GeocodingRepository
) {

    suspend operator fun invoke(
        coordinates: Coordinates
    ): AppResult<String> =
        geocodingRepository.getAddressFromCoordinates(coordinates)

}
