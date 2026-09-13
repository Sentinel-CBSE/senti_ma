package com.example.senti_ma.domain.usecase

import com.example.senti_ma.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsUseCases @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    fun observeDarkTheme(): Flow<Boolean> =
        settingsRepository.isDarkTheme

    suspend fun setDarkTheme(enabled: Boolean) =
        settingsRepository.setDarkTheme(enabled)

}
