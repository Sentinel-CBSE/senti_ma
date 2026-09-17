package com.unal.senti_ma.data.location

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.location.LocationClient
import com.unal.senti_ma.domain.model.Coordinates
import com.unal.senti_ma.domain.repository.LocationRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class LocationForegroundService : Service() {

    @Inject
    lateinit var locationClient: LocationClient

    @Inject
    lateinit var locationRepository: LocationRepository

    private val serviceScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    private var locationJob: Job? = null
    private var backendJob: Job? = null

    private var lastLocation: Coordinates? = null

    companion object {
        private const val CHANNEL_ID = "location_tracking"
        private const val NOTIFICATION_ID = 1001

        private const val BACKEND_UPDATE_INTERVAL = 60_000L
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
        startForeground(
            NOTIFICATION_ID,
            createNotification(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (locationJob == null || locationJob?.isActive == false) {
            startLocationTracking()
        }
        if (backendJob == null || backendJob?.isActive == false) {
            startBackendTracking()
        }
        return START_STICKY
    }

    private fun startLocationTracking() {
        locationClient.startLocationUpdates()

        locationJob = serviceScope.launch {
            locationClient.currentLocation.collectLatest { location ->
                lastLocation = location
            }
        }
    }

    private fun startBackendTracking() {
        backendJob = serviceScope.launch {

            while (isActive) {
                delay(BACKEND_UPDATE_INTERVAL.milliseconds)
                lastLocation?.let { location ->
                    sendLocationToBackend(location)
                }
            }
        }
    }

    private suspend fun sendLocationToBackend(
        location: Coordinates
    ) {
        locationRepository.sendLocation(location)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.notification_location_channel),
            NotificationManager.IMPORTANCE_LOW
        )

        val notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_location_tracking))
            .setSmallIcon(R.drawable.ic_location)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        locationJob?.cancel()
        backendJob?.cancel()

        locationClient.stopLocationUpdates()

        serviceScope.cancel()

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}
