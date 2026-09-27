package com.unal.senti_ma.data.remote.dto

import android.net.Uri
import com.unal.senti_ma.domain.enums.BloodTypeLetter
import com.unal.senti_ma.domain.enums.BloodTypeRh

data class UserDto(
    val uid: String,
    val email: String,
    val photoUrl: Uri?,
    val displayName: String,
    val isAnonymous: Boolean,
    val isEmailVerified: Boolean,
    val bloodTypeRh: BloodTypeRh,
    val bloodTypeLetter: BloodTypeLetter,
    val emergencyContacts: List<EmergencyContactDto>,
    val eps: String?
)
