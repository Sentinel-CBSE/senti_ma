package com.unal.senti_ma.domain.usecase.permissions

import com.unal.senti_ma.domain.repository.PermissionRepository
import javax.inject.Inject

class UpdateLocationPermissionResultUseCase @Inject constructor(
    private val permissionRepository: PermissionRepository
) {

    operator fun invoke(granted: Boolean) {
        permissionRepository.updateLocationPermissionResult(granted)
    }

}
