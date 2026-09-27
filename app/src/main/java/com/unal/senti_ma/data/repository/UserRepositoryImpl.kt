package com.unal.senti_ma.data.repository

import android.content.Context
import android.util.Log
import com.unal.senti_ma.R
import com.unal.senti_ma.data.mappers.toDomain
import com.unal.senti_ma.data.remote.api.SentinelApi
import com.unal.senti_ma.data.remote.dto.EmergencyContactRequestDto
import com.unal.senti_ma.data.remote.dto.UserUpdateDto
import com.unal.senti_ma.domain.enums.BloodTypeLetter
import com.unal.senti_ma.domain.enums.BloodTypeRh
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.EmergencyContact
import com.unal.senti_ma.domain.model.User
import com.unal.senti_ma.domain.model.UserUpdate
import com.unal.senti_ma.domain.repository.UserRepository
import com.unal.senti_ma.utils.logIfError
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

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

        Log.d(
            "UserRepository",
            "Updating user profile"
        )

        return try {

            val response = sentinelApi.updateProfile(
                UserUpdateDto(
                    displayName = update.displayName,
                    bloodTypeRh = update.bloodTypeRh,
                    bloodTypeLetter = update.bloodTypeLetter,
                    eps = update.eps
                )
            )

            if (response.logIfError(
                    "UserRepository",
                    "Update profile"
                )
            ) {

                val user = response.body()?.toDomain()

                if (user != null) {
                    Log.d(
                        "UserRepository",
                        "Profile updated successfully"
                    )

                    AppResult.Success(user)
                } else {
                    AppResult.Error(
                        context.getString(
                            R.string.text_error_update_profile
                        )
                    )
                }

            } else {
                AppResult.Error(
                    context.getString(
                        R.string.text_error_update_profile
                    )
                )
            }

        } catch (exception: Exception) {

            Log.e(
                "UserRepository",
                "Error updating profile",
                exception
            )

            AppResult.Error(
                context.getString(R.string.text_error_update_profile)
            )
        }
    }

    override suspend fun addEmergencyContact(
        contact: EmergencyContact
    ): AppResult<User> {

        Log.d(
            "UserRepository",
            "Adding emergency contact: uid=${contact.uid}"
        )

        return try {

            val response = sentinelApi.addEmergencyContact(
                EmergencyContactRequestDto(
                    name = contact.name,
                    phoneNumber = contact.phoneNumber,
                    relationship = contact.relationship
                )
            )

            if (response.logIfError(
                    "UserRepository",
                    "Add emergency contact"
                )
            ) {

                val user = response.body()?.toDomain()

                if (user != null) {
                    Log.d(
                        "UserRepository",
                        "Emergency contact added successfully"
                    )

                    AppResult.Success(user)
                } else {
                    AppResult.Error(
                        context.getString(
                            R.string.text_error_update_profile
                        )
                    )
                }

            } else {
                AppResult.Error(
                    context.getString(
                        R.string.text_error_update_profile
                    )
                )
            }

        } catch (exception: Exception) {

            Log.e(
                "UserRepository",
                "Error adding emergency contact",
                exception
            )

            AppResult.Error(
                context.getString(R.string.text_error_update_profile)
            )
        }
    }

    override suspend fun updateEmergencyContact(
        contact: EmergencyContact
    ): AppResult<User> {

        Log.d(
            "UserRepository",
            "Updating emergency contact: uid=${contact.uid}"
        )

        return try {
            val response = sentinelApi.updateEmergencyContact(
                uid = contact.uid,
                contact = EmergencyContactRequestDto(
                    name = contact.name,
                    phoneNumber = contact.phoneNumber,
                    relationship = contact.relationship
                )
            )

            if (response.logIfError(
                    "UserRepository",
                    "Update emergency contact"
                )
            ) {

                val user = response.body()?.toDomain()

                if (user != null) {
                    Log.d(
                        "UserRepository",
                        "Emergency contact updated successfully"
                    )

                    AppResult.Success(user)
                } else {
                    AppResult.Error(
                        context.getString(
                            R.string.text_error_update_profile
                        )
                    )
                }

            } else {
                AppResult.Error(
                    context.getString(
                        R.string.text_error_update_profile
                    )
                )
            }

        } catch (exception: Exception) {

            Log.e(
                "UserRepository",
                "Error updating emergency contact",
                exception
            )

            AppResult.Error(
                context.getString(R.string.text_error_update_profile)
            )
        }
    }

    override suspend fun deleteEmergencyContact(
        contactUid: String
    ): AppResult<User> {

        Log.d(
            "UserRepository",
            "Deleting emergency contact: uid=$contactUid"
        )

        return try {
            val response = sentinelApi.deleteEmergencyContact(
                uid = contactUid
            )

            if (response.logIfError(
                    "UserRepository",
                    "Delete emergency contact"
                )
            ) {

                val user = response.body()?.toDomain()

                if (user != null) {
                    Log.d(
                        "UserRepository",
                        "Emergency contact deleted successfully"
                    )

                    AppResult.Success(user)
                } else {
                    AppResult.Error(
                        context.getString(
                            R.string.text_error_update_profile
                        )
                    )
                }

            } else {
                AppResult.Error(
                    context.getString(
                        R.string.text_error_update_profile
                    )
                )
            }

        } catch (exception: Exception) {

            Log.e(
                "UserRepository",
                "Error deleting emergency contact",
                exception
            )

            AppResult.Error(
                context.getString(R.string.text_error_update_profile)
            )
        }
    }

}
