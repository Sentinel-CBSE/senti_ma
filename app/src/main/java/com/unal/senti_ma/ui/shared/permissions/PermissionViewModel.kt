package com.unal.senti_ma.ui.shared.permissions

import androidx.lifecycle.ViewModel
import com.unal.senti_ma.domain.enums.PermissionStatus
import com.unal.senti_ma.domain.usecase.permissions.ObserveBackgroundLocationPermissionUseCase
import com.unal.senti_ma.domain.usecase.permissions.ObserveLocationPermissionUseCase
import com.unal.senti_ma.domain.usecase.permissions.RefreshLocationPermissionUseCase
import com.unal.senti_ma.domain.usecase.permissions.UpdateLocationPermissionResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    observeLocationPermissionUseCase: ObserveLocationPermissionUseCase,
    observeBackgroundLocationPermissionUseCase: ObserveBackgroundLocationPermissionUseCase,
    private val refreshLocationPermissionUseCase: RefreshLocationPermissionUseCase,
    private val updateLocationPermissionResultUseCase: UpdateLocationPermissionResultUseCase
) : ViewModel() {

    val locationPermissionStatus: StateFlow<PermissionStatus> =
        observeLocationPermissionUseCase()

    val backgroundLocationPermissionGranted: StateFlow<Boolean> =
        observeBackgroundLocationPermissionUseCase()

    fun onLocationPermissionResult(granted: Boolean) {
        updateLocationPermissionResultUseCase(granted)
    }

    fun refreshLocationPermission() {
        refreshLocationPermissionUseCase()
    }

}
