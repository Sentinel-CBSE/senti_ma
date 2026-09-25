package com.unal.senti_ma.domain.usecase.permissions

import com.unal.senti_ma.domain.enums.PermissionStatus
import com.unal.senti_ma.domain.repository.PermissionRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveLocationPermissionUseCase @Inject constructor(
    private val permissionRepository: PermissionRepository
) {

    operator fun invoke(): StateFlow<PermissionStatus> =
        permissionRepository.locationPermissionStatus

}
