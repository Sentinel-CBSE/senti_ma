package com.unal.senti_ma.domain.usecase.location

import com.unal.senti_ma.domain.location.LocationClient
import com.unal.senti_ma.domain.model.Coordinates
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveCurrentLocationUseCase @Inject constructor(
    private val locationClient: LocationClient
) {

    operator fun invoke(): StateFlow<Coordinates?> =
        locationClient.currentLocation

}
