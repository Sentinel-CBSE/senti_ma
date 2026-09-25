package com.unal.senti_ma.domain.repository

import com.unal.senti_ma.domain.enums.PermissionStatus
import kotlinx.coroutines.flow.StateFlow

interface PermissionRepository {

    val locationPermissionStatus: StateFlow<PermissionStatus>

    val backgroundLocationPermissionGranted: StateFlow<Boolean>

    fun refreshLocationPermission()

    fun updateLocationPermissionResult(granted: Boolean)

}
