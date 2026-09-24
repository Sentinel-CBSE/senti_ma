package com.unal.senti_ma.domain.usecase.user

import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.EmergencyContact
import com.unal.senti_ma.domain.model.User
import com.unal.senti_ma.domain.repository.UserRepository
import javax.inject.Inject

class UpdateEmergencyContactUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        contact: EmergencyContact
    ): AppResult<User> =
        userRepository.updateEmergencyContact(contact)

}
