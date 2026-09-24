package com.unal.senti_ma.domain.usecase.location

import com.unal.senti_ma.domain.location.LocationTracker
import javax.inject.Inject

class StopLocationTrackingUseCase @Inject constructor(
    private val locationTracker: LocationTracker
) {

    operator fun invoke() {
        locationTracker.stop()
    }

}
