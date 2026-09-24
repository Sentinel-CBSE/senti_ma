package com.unal.senti_ma.domain.usecase.settings

import com.unal.senti_ma.domain.repository.SettingsRepository
import javax.inject.Inject

class SetLocationTrackingEnabledUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(enabled: Boolean) {
        settingsRepository.setLocationTrackingEnabled(enabled)
    }

}
