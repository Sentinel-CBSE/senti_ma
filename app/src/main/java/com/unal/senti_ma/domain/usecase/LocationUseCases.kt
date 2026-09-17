package com.unal.senti_ma.domain.usecase

import com.unal.senti_ma.domain.location.LocationTracker
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationUseCases @Inject constructor(
    private val locationTracker: LocationTracker
) {

    fun startTracking() {
        locationTracker.start()
    }

    fun stopTracking() {
        locationTracker.stop()
    }

}
