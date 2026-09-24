package com.unal.senti_ma.domain.usecase.location

import com.unal.senti_ma.domain.location.LocationClient
import com.unal.senti_ma.domain.location.LocationSubscription
import javax.inject.Inject

class AcquireLocationUpdatesUseCase @Inject constructor(
    private val locationClient: LocationClient
) {

    operator fun invoke(): LocationSubscription =
        locationClient.acquireLocationUpdates()

}
