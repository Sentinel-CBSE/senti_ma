package com.unal.senti_ma.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.domain.usecase.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean?> = settingsUseCases.observeDarkTheme()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            settingsUseCases.setDarkTheme(enabled)
        }
    }

}
