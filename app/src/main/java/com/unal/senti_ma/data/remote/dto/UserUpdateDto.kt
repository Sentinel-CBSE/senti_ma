package com.unal.senti_ma.data.remote.dto

import com.unal.senti_ma.domain.enums.BloodTypeLetter
import com.unal.senti_ma.domain.enums.BloodTypeRh

data class UserUpdateDto(
    val displayName: String?,
    val bloodTypeRh: BloodTypeRh?,
    val bloodTypeLetter: BloodTypeLetter?,
    val eps: String?
)
