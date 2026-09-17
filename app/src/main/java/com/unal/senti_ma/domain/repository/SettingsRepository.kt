package com.unal.senti_ma.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val isDarkTheme: Flow<Boolean>

    val isLocationTrackingEnabled: Flow<Boolean>

    suspend fun setDarkTheme(enabled: Boolean)

    suspend fun setLocationTrackingEnabled(enabled: Boolean)

}
