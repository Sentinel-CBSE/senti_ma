package com.example.senti_ma.domain.usecase

import com.example.senti_ma.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Groups all settings/preferences use cases behind a single entry point.
 * ViewModels depend on this instead of the repository directly, keeping
 * the data source (DataStore, or a future remote config) fully isolated.
 */
@Singleton
class SettingsUseCases @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    fun observeDarkTheme(): Flow<Boolean> =
        settingsRepository.isDarkTheme

    suspend fun setDarkTheme(enabled: Boolean) =
        settingsRepository.setDarkTheme(enabled)
}
