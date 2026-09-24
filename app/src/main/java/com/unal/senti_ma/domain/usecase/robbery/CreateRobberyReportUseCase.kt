package com.unal.senti_ma.domain.usecase.robbery

import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.repository.RobberyRepository
import javax.inject.Inject

class CreateRobberyReportUseCase @Inject constructor(
    private val robberyRepository: RobberyRepository
) {

    suspend operator fun invoke(
        type: String,
        latitude: Double,
        longitude: Double
    ): AppResult<Unit> =
        robberyRepository.createRobberyReport(
            type = type,
            latitude = latitude,
            longitude = longitude,
            timestamp = System.currentTimeMillis()
        )

}
