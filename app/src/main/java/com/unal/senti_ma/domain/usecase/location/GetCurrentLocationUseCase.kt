package com.unal.senti_ma.domain.usecase.location

import com.unal.senti_ma.domain.location.LocationClient
import com.unal.senti_ma.domain.model.Coordinates
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationClient: LocationClient
) {

    suspend operator fun invoke(): Coordinates? =
        locationClient.getCurrentLocation()

}
