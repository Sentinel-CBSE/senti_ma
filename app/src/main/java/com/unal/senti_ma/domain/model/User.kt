package com.unal.senti_ma.domain.model

import android.net.Uri
import com.unal.senti_ma.domain.enums.BloodTypeLetter
import com.unal.senti_ma.domain.enums.BloodTypeRh

data class User(
    val uid: String,
    val email: String?,
    val photoUrl: Uri?,
    val displayName: String?,
    val isAnonymous: Boolean,
    val isEmailVerified: Boolean,
    val emergencyContacts: List<EmergencyContact>,
    val bloodTypeLetter: BloodTypeLetter?,
    val bloodTypeRh: BloodTypeRh?,
    val eps: String?
)
