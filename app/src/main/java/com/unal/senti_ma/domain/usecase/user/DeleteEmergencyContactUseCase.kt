package com.unal.senti_ma.domain.usecase.user

import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.User
import com.unal.senti_ma.domain.repository.UserRepository
import javax.inject.Inject

class DeleteEmergencyContactUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        contactUid: String
    ): AppResult<User> =
        userRepository.deleteEmergencyContact(contactUid)

}
