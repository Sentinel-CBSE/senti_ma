package com.unal.senti_ma.domain.usecase.location

import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.Coordinates
import com.unal.senti_ma.domain.repository.LocationRepository
import javax.inject.Inject

class SendLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {

    suspend operator fun invoke(
        coordinates: Coordinates
    ): AppResult<Unit> =
        locationRepository.sendLocation(coordinates)

}
