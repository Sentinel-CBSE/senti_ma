package com.unal.senti_ma.domain.usecase

import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.EmergencyContact
import com.unal.senti_ma.domain.model.User
import com.unal.senti_ma.domain.model.UserUpdate
import com.unal.senti_ma.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserUseCases @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun updateProfile(
        update: UserUpdate
    ): AppResult<User> =
        userRepository.updateProfile(update)

    suspend fun addEmergencyContact(
        contact: EmergencyContact
    ): AppResult<User> =
        userRepository.addEmergencyContact(contact)

    suspend fun updateEmergencyContact(
        contact: EmergencyContact
    ): AppResult<User> =
        userRepository.updateEmergencyContact(contact)

    suspend fun deleteEmergencyContact(
        contactUid: String
    ): AppResult<User> =
        userRepository.deleteEmergencyContact(contactUid)

}
