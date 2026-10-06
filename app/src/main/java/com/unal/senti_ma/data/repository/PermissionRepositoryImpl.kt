package com.unal.senti_ma.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.unal.senti_ma.domain.enums.PermissionStatus
import com.unal.senti_ma.domain.repository.PermissionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PermissionRepository {

    private val _locationPermissionStatus = MutableStateFlow(checkLocationPermissionStatus())
    override val locationPermissionStatus: StateFlow<PermissionStatus> =
        _locationPermissionStatus.asStateFlow()

    private val _backgroundLocationPermissionGranted =
        MutableStateFlow(checkBackgroundLocationPermission())
    override val backgroundLocationPermissionGranted: StateFlow<Boolean> =
        _backgroundLocationPermissionGranted.asStateFlow()

    override fun refreshLocationPermission() {
        _locationPermissionStatus.value =
            checkLocationPermissionStatus()

        _backgroundLocationPermissionGranted.value =
            checkBackgroundLocationPermission()
    }

    override fun updateLocationPermissionResult(granted: Boolean) {
        _locationPermissionStatus.value =
            if (granted) {
                PermissionStatus.GRANTED
            } else {
                PermissionStatus.DENIED
            }

        _backgroundLocationPermissionGranted.value =
            checkBackgroundLocationPermission()
    }

    private fun checkLocationPermissionStatus(): PermissionStatus {
        val fineGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        return if (fineGranted || coarseGranted) {
            PermissionStatus.GRANTED
        } else {
            PermissionStatus.DENIED
        }
    }

    private fun checkBackgroundLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

}
