package com.unal.senti_ma.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.domain.usecase.LocationUseCases
import com.unal.senti_ma.domain.usecase.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationUseCases: LocationUseCases,
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    val isLocationTrackingEnabled: StateFlow<Boolean> =
        settingsUseCases.observeLocationTracking()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    fun startTracking() {
        locationUseCases.startTracking()

        viewModelScope.launch {
            settingsUseCases.setLocationTrackingEnabled(true)
        }
    }

    fun stopTracking() {
        locationUseCases.stopTracking()

        viewModelScope.launch {
            settingsUseCases.setLocationTrackingEnabled(false)
        }
    }

}
