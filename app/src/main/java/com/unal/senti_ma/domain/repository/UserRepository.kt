package com.unal.senti_ma.domain.repository

import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.EmergencyContact
import com.unal.senti_ma.domain.model.User
import com.unal.senti_ma.domain.model.UserUpdate

interface UserRepository {

    suspend fun getProfile(): AppResult<User>

    suspend fun updateProfile(
        update: UserUpdate
    ): AppResult<User>

    suspend fun addEmergencyContact(
        contact: EmergencyContact
    ): AppResult<User>

    suspend fun updateEmergencyContact(
        contact: EmergencyContact
    ): AppResult<User>

    suspend fun deleteEmergencyContact(
        contactUid: String
    ): AppResult<User>

}
