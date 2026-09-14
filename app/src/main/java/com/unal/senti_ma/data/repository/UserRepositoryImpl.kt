package com.unal.senti_ma.data.repository

import android.content.Context
import com.unal.senti_ma.R
import com.unal.senti_ma.data.remote.api.SentinelApi
import com.unal.senti_ma.domain.enums.BloodTypeLetter
import com.unal.senti_ma.domain.enums.BloodTypeRh
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.EmergencyContact
import com.unal.senti_ma.domain.model.User
import com.unal.senti_ma.domain.model.UserUpdate
import com.unal.senti_ma.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class UserRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sentinelApi: SentinelApi
) : UserRepository {

    private var fakeUser = User(
        uid = "fake-user",
        email = "david@example.com",
        photoUrl = null,
        displayName = "David",
        isAnonymous = false,
        isEmailVerified = true,
        bloodTypeRh = BloodTypeRh.POSITIVE,
        bloodTypeLetter = BloodTypeLetter.O,
        emergencyContacts = listOf(
            EmergencyContact(
                uid = "contact-1",
                name = "Laura",
                phoneNumber = "3001234567",
                relationship = "Hermana"
            ),
            EmergencyContact(
                uid = "contact-2",
                name = "Carlos",
                phoneNumber = "3109876543",
                relationship = "Padre"
            )
        ),
        eps = "SURA"
    )

    override suspend fun updateProfile(
        update: UserUpdate
    ): AppResult<User> {
        return try {
            delay(500.milliseconds)

            fakeUser = fakeUser.copy(
                displayName = update.displayName ?: fakeUser.displayName,
                bloodTypeLetter = update.bloodTypeLetter
                    ?: fakeUser.bloodTypeLetter,
                bloodTypeRh = update.bloodTypeRh
                    ?: fakeUser.bloodTypeRh,
                eps = update.eps ?: fakeUser.eps
            )

            AppResult.Success(fakeUser)

        } catch (_: Exception) {
            AppResult.Error(
                context.getString(R.string.text_error_update_profile)
            )
        }
    }

    override suspend fun addEmergencyContact(
        contact: EmergencyContact
    ): AppResult<User> {
        return try {
            delay(500.milliseconds)

            fakeUser = fakeUser.copy(
                emergencyContacts = fakeUser.emergencyContacts + contact
            )

            AppResult.Success(fakeUser)

        } catch (_: Exception) {
            AppResult.Error(
                context.getString(R.string.text_error_update_profile)
            )
        }
    }

    override suspend fun updateEmergencyContact(
        contact: EmergencyContact
    ): AppResult<User> {
        return try {
            delay(500.milliseconds)

            fakeUser = fakeUser.copy(
                emergencyContacts = fakeUser.emergencyContacts.map {
                    if (it.uid == contact.uid) {
                        contact
                    } else {
                        it
                    }
                }
            )

            AppResult.Success(fakeUser)

        } catch (_: Exception) {
            AppResult.Error(
                context.getString(R.string.text_error_update_profile)
            )
        }
    }

    override suspend fun deleteEmergencyContact(
        contactUid: String
    ): AppResult<User> {
        return try {
            delay(500.milliseconds)

            fakeUser = fakeUser.copy(
                emergencyContacts = fakeUser.emergencyContacts.filterNot {
                    it.uid == contactUid
                }
            )

            AppResult.Success(fakeUser)

        } catch (_: Exception) {
            AppResult.Error(
                context.getString(R.string.text_error_update_profile)
            )
        }
    }

}
