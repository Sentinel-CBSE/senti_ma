package com.unal.senti_ma.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.unal.senti_ma.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private val darkThemeKey =
        booleanPreferencesKey("is_dark_theme")
    private val locationTrackingEnabledKey =
        booleanPreferencesKey("location_tracking_enabled")

    override val isDarkTheme: Flow<Boolean> =
        dataStore.data.map { prefs ->
            prefs[darkThemeKey] ?: false
        }

    override val isLocationTrackingEnabled: Flow<Boolean> =
        dataStore.data.map { prefs ->
            prefs[locationTrackingEnabledKey] ?: false
        }

    override suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[darkThemeKey] = enabled
        }
    }

    override suspend fun setLocationTrackingEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[locationTrackingEnabledKey] = enabled
        }
    }

}
