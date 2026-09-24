package com.unal.senti_ma.domain.usecase.settings

import com.unal.senti_ma.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLocationTrackingUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<Boolean> =
        settingsRepository.isLocationTrackingEnabled

}
