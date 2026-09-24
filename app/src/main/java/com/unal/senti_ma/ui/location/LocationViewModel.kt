package com.unal.senti_ma.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.domain.usecase.location.StartLocationTrackingUseCase
import com.unal.senti_ma.domain.usecase.location.StopLocationTrackingUseCase
import com.unal.senti_ma.domain.usecase.settings.ObserveLocationTrackingUseCase
import com.unal.senti_ma.domain.usecase.settings.SetLocationTrackingEnabledUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val setLocationTrackingEnabledUseCase: SetLocationTrackingEnabledUseCase,
    private val startLocationTrackingUseCase: StartLocationTrackingUseCase,
    private val stopLocationTrackingUseCase: StopLocationTrackingUseCase,
    observeLocationTrackingUseCase: ObserveLocationTrackingUseCase
) : ViewModel() {

    val isLocationTrackingEnabled: StateFlow<Boolean> =
        observeLocationTrackingUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    fun startTracking() {
        startLocationTrackingUseCase()

        viewModelScope.launch {
            setLocationTrackingEnabledUseCase(true)
        }
    }

    fun stopTracking() {
        stopLocationTrackingUseCase()

        viewModelScope.launch {
            setLocationTrackingEnabledUseCase(false)
        }
    }

}
