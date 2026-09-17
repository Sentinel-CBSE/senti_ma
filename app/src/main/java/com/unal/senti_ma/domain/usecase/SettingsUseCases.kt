package com.unal.senti_ma.domain.usecase

import com.unal.senti_ma.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsUseCases @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    fun observeDarkTheme(): Flow<Boolean> =
        settingsRepository.isDarkTheme

    fun observeLocationTracking(): Flow<Boolean> =
        settingsRepository.isLocationTrackingEnabled

    suspend fun setDarkTheme(enabled: Boolean) =
        settingsRepository.setDarkTheme(enabled)

    suspend fun setLocationTrackingEnabled(enabled: Boolean) {
        settingsRepository.setLocationTrackingEnabled(enabled)
    }

}
