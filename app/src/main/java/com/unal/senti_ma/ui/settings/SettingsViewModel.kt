package com.unal.senti_ma.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.domain.usecase.settings.ObserveDarkThemeUseCase
import com.unal.senti_ma.domain.usecase.settings.ObserveLocationTrackingUseCase
import com.unal.senti_ma.domain.usecase.settings.SetDarkThemeUseCase
import com.unal.senti_ma.domain.usecase.settings.SetLocationTrackingEnabledUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeDarkThemeUseCase: ObserveDarkThemeUseCase,
    observeLocationTrackingUseCase: ObserveLocationTrackingUseCase,
    private val setDarkThemeUseCase: SetDarkThemeUseCase,
    private val setLocationTrackingEnabledUseCase: SetLocationTrackingEnabledUseCase
) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean?> =
        observeDarkThemeUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val isLocationTrackingEnabled: StateFlow<Boolean> =
        observeLocationTrackingUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            setDarkThemeUseCase(enabled)
        }
    }

    fun setLocationTrackingEnabled(enabled: Boolean) {
        viewModelScope.launch {
            setLocationTrackingEnabledUseCase(enabled)
        }
    }

}
