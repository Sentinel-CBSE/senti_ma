package com.unal.senti_ma.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.unal.senti_ma.data.mappers.toDomain
import com.unal.senti_ma.domain.location.LocationClient
import com.unal.senti_ma.domain.location.LocationSubscription
import com.unal.senti_ma.domain.model.Coordinates
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class LocationManager @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationClient {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val _currentLocation = MutableStateFlow<Coordinates?>(null)
    override val currentLocation: StateFlow<Coordinates?> = _currentLocation.asStateFlow()
    private var activeSubscriptions = 0
    private val lock = Any()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                _currentLocation.value = location.toDomain()
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun acquireLocationUpdates(): LocationSubscription {
        val subscription = Subscription()

        synchronized(lock) {
            val wasInactive = activeSubscriptions == 0
            activeSubscriptions++

            if (wasInactive) {
                val locationRequest = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    15_000L
                )
                    .setMinUpdateIntervalMillis(10_000L)
                    .build()

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }

        return subscription
    }

    private inner class Subscription : LocationSubscription {

        private var closed = false

        override fun close() {
            synchronized(lock) {
                if (closed) {
                    return
                }

                closed = true
                activeSubscriptions--

                if (activeSubscriptions == 0) {
                    fusedLocationClient.removeLocationUpdates(
                        locationCallback
                    )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Coordinates? {
        return suspendCancellableCoroutine { continuation ->
            val cancellationTokenSource = CancellationTokenSource()

            val task = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )

            task.addOnSuccessListener { location ->
                if (continuation.isActive) {
                    continuation.resume(
                        location?.toDomain()
                    )
                }
            }

            task.addOnFailureListener {
                if (continuation.isActive) {
                    continuation.resume(null)
                }
            }

            continuation.invokeOnCancellation {
                cancellationTokenSource.cancel()
            }
        }
    }

}
