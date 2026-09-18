package com.unal.senti_ma.domain.usecase

import com.unal.senti_ma.domain.location.LocationClient
import com.unal.senti_ma.domain.location.LocationSubscription
import com.unal.senti_ma.domain.location.LocationTracker
import com.unal.senti_ma.domain.model.Coordinates
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationUseCases @Inject constructor(
    private val locationTracker: LocationTracker,
    private val locationClient: LocationClient
) {

    fun startTracking() {
        locationTracker.start()
    }

    fun stopTracking() {
        locationTracker.stop()
    }

    fun acquireLocationUpdates(): LocationSubscription {
        return locationClient.acquireLocationUpdates()
    }

    fun observeCurrentLocation(): StateFlow<Coordinates?> {
        return locationClient.currentLocation
    }

    suspend fun getCurrentLocation(): Coordinates? {
        return locationClient.getCurrentLocation()
    }

}
