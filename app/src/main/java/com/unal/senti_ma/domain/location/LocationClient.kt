package com.unal.senti_ma.domain.location

import com.unal.senti_ma.domain.model.Coordinates
import kotlinx.coroutines.flow.StateFlow

interface LocationClient {

    val currentLocation: StateFlow<Coordinates?>

    fun acquireLocationUpdates(): LocationSubscription

    suspend fun getCurrentLocation(): Coordinates?

}
