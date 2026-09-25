package com.unal.senti_ma.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.domain.usecase.location.StartLocationTrackingUseCase
import com.unal.senti_ma.domain.usecase.location.StopLocationTrackingUseCase
import com.unal.senti_ma.domain.usecase.settings.SetLocationTrackingEnabledUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val setLocationTrackingEnabledUseCase: SetLocationTrackingEnabledUseCase,
    private val startLocationTrackingUseCase: StartLocationTrackingUseCase,
    private val stopLocationTrackingUseCase: StopLocationTrackingUseCase,
) : ViewModel() {

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
